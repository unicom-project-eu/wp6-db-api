import json
import string


class AttributeInfo:
    def __init__(self, is_key=False, set_value=lambda x: None):
        self.set_value = set_value
        self.is_key = is_key
    pass

def csv_mapping(**attributes):
    def decorator(cls):
        old_init = cls.__init__
        def new_init(self, x, *args, **kwargs):
            old_init(self, *args, **kwargs)

            for attr, info in attributes.items():
                setattr(self, attr, info.set_value(x) if info.set_value else None)

            pass
        cls.__init__ = new_init

        def get_key(self):
            return tuple(getattr(self, attr) for attr, info in attributes.items() if info.is_key)
        cls.get_key = get_key

        cls.__repr__ = lambda self: json.dumps(self.__dict__, indent=2, default=lambda o: o.__dict__)

        cls.__eq__ = lambda self, other: self.__dict__ == other.__dict__

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