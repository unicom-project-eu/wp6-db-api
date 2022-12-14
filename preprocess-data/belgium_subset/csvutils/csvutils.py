import json
import string


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
                    print(
                            '------------------------------\n'
                        +   'CONFLICT\n'
                        +   f'key: {key}\n'
                        +   f'old: {instances_dict[key]}\n\n'
                        +   f'new: {self}\n'
                        +   '------------------------------\n'
                    )
                    raise Exception(f'Conflicting values for key {key}')
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