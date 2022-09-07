from collections import defaultdict
import csv
import json

from csvutils.csvutils import *

def mocked_value(val):
    return f'MOCK-{val}'

ORIGINA_PATH = 'original.csv'

@csv_mapping(
    code=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['edqmformid'])),
)
class DoseForm:
    pass

@csv_mapping(
    ingredientCode=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['ingredient_code'])),
    moiety=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['moiety'])),
    modifier=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['d_modifier'])),
)
class SubstanceWithRolePai:
    pass

@csv_mapping(
    phpId=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['PhPID_MD5'])),
    preciseActiveIngredient=AttributeInfo(set_value=lambda x: SubstanceWithRolePai(x))
)
class PharmaceuticalProduct:
    pass

@csv_mapping(
    mpId=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['cnk_pub'])),
    country=AttributeInfo(set_value=lambda x: 'bel'),
    fullName=AttributeInfo(set_value=lambda x: string_or_none(x['medicinal_product_name'])),
    pharmaceuticalProduct=AttributeInfo(set_value=lambda x: PharmaceuticalProduct(x)),
)
class MedicinalProduct:
    pass

@csv_mapping(
    pcId=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['a_Id'])),
    packageSize=AttributeInfo(set_value=lambda x: int_or_none(x['packdisplayvalue'])),
    atcCode=AttributeInfo(set_value=lambda x: string_or_none(x['atc'])),
    medicinalProduct=AttributeInfo(set_value=lambda x: MedicinalProduct(x))
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')

    packages = {}
    medicinal_products = {}
    pharmaceutical_products = {}
    def print_results(dct, cls):
        print(f'\n\nFound {len(dct)} instances of {cls.__name__}')
        for key, val in dct.items():
            print(f'{key}: {val}\n')
        pass

    with open(ORIGINA_PATH, 'r') as csvin:
        csvreader = csv.DictReader(csvin, delimiter=',')

        for row in csvreader:
            pharmaceutical_product = PharmaceuticalProduct(row)
            pharmaceutical_products[pharmaceutical_product.get_key()] = pharmaceutical_product
            
            medicinal_product = MedicinalProduct(row)
            medicinal_products[medicinal_product.get_key()] = medicinal_product

            packaged_medicinal_product = PackagedMedicinalProduct(row)
            packages[packaged_medicinal_product.get_key()] = packaged_medicinal_product
    

        print_results(pharmaceutical_products, PharmaceuticalProduct)
        print_results(medicinal_products, MedicinalProduct)
        print_results(packages, PackagedMedicinalProduct)


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
