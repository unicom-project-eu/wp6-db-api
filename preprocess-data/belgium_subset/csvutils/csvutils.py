import json
import string
import difflib
from colorama import Fore, Style

class ConflictException(Exception):
    def __init__(self, key, old, new):
        self.key = key
        self.old = old
        self.new = new       

        self.message = self.get_message()
        super().__init__(self.message)
    
    def get_message(self, color=False):
        def set_color(s):
            if not color:
                return s
            if len(s) == 0:
                return ''
            c = s[0]
            pre = ''
            if (c == '+'):
                pre = Fore.GREEN
            elif (c == '-'):
                pre = Fore.RED
            elif (c == '?'):
                pre = f'{Fore.WHITE}{Style.BRIGHT}'
            else:
                return s
            return f'{pre}{s}{Fore.RESET}{Style.RESET_ALL}'

        differ = difflib.Differ()
        diff = '\n'.join([set_color(s) for s in differ.compare(f'{self.old}'.split('\n'), f'{self.new}'.split('\n'))])

        message =  '\n------------------------------\n'
        message += f'CONFLICT for key {self.key}\n'
        message += f'diff:\n{diff}'
        message += '\n------------------------------\n'
        return message
    pass

class AttributeInfo:
    def __init__(self, is_key=False, set_value=lambda x: None, is_hidden=False):
        self.set_value = set_value
        self.is_key = is_key
        self.is_hidden = is_hidden
    pass

def csv_mapping(**attributes):
    def decorator(cls):
        instances_dict = {}

        def non_hidden_dict(self):
            hidden = {
                attr
                for attr, info in attributes.items()
                if info.is_hidden
            }
            return {
                k: v
                for k, v in self.__dict__.items() 
                if k not in hidden
            }
        cls.non_hidden_dict = non_hidden_dict
        cls.__repr__ = lambda self: json.dumps(self.non_hidden_dict(), indent=2, default=lambda o: o.non_hidden_dict() if hasattr(o, 'non_hidden_dict') else o.__dict__)
        cls.__eq__ = lambda self, other: self.__dict__ == other.__dict__

        def get_key(self):
            return tuple(getattr(self, attr) for attr, info in attributes.items() if info.is_key)
        cls.get_key = get_key

        old_init = cls.__init__
        def new_init(self, x, *args, **kwargs):
            old_init(self, *args, **kwargs)

            for attr, info in attributes.items():
                setattr(self, attr, info.set_value(x) if info.set_value else None)
            
            key = self.get_key()
            if key in instances_dict:
                if instances_dict[key] != self:
                    raise ConflictException(key=key, old=instances_dict[key], new=self)
            instances_dict[key] = self
            pass
        cls.__init__ = new_init

        return cls
    return decorator

def string_or_none(s):
    return s if s and s != '' else None

def int_or_none(s):
    s = string_or_none(s)
    return int(s) if s else None

def comma_separated_str_to_list(s: string):
    if s is None:
        return []
    return list(map(lambda i: i.strip(), s.split()))