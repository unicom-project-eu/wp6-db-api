from collections import defaultdict
import csv
import re
import itertools
from struct import pack

def mocked_value(val):
    return f'MOCK-{val}'

ORIGINA_PATH = "original.csv"


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

        cls.__repr__ = lambda self: str(self.__dict__) 

        return cls
    return decorator



@csv_mapping(
    mpId=AttributeInfo(is_key=True, set_value=lambda x: mocked_value())
)
class MedicinalProduct:
    pass


@csv_mapping(
    pcId=AttributeInfo(is_key=True, set_value=lambda x: mocked_value(hash(x['AmppName']))),
    packageSize=AttributeInfo(set_value=lambda x: mocked_value(9999)),
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')

    packages = {}
    medicinal_products = {}
    pharmaceutical_product = {}

    with open(ORIGINA_PATH, 'r') as csvin:
        csvreader = csv.DictReader(csvin, delimiter=',')

        for row in csvreader:
            packaged_medicinal_product = PackagedMedicinalProduct(row)
            packages[packaged_medicinal_product.get_key()] = packaged_medicinal_product
    
    print(packages)
            


    # print(f'Writing output to "{OUTPUT_PATH}"...')

    # with open(OUTPUT_PATH, 'w') as csvout:
    #     csvwriter = csv.DictWriter(csvout, list((output_rows[0].keys())))
    #     csvwriter.writeheader()

    #     for row in output_rows:
    #         csvwriter.writerow({k: 'NULL' if v is None else v for k, v in row.items()})
    
    # for code, rows in rows_by_ingredient.items():
    #     if len(rows) > 1:
    #         print(f'WARN - Duplicates for ingredient_code {code}: {rows}')

    # print('Done')
