from collections import defaultdict
import csv
import json

from csvutils.csvutils import *

def mocked_value(val):
    return f'MOCK-{val}'

ORIGINA_PATH = 'original.csv'

@csv_mapping(
    code=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['code'])),
    display=AttributeInfo(set_value=lambda x: string_or_none(x['display'])),
)
class EdqmDoseForm:
    pass

@csv_mapping(
    code=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['code'])),
    display=AttributeInfo(set_value=lambda x: string_or_none(x['display'])),
)
class EdqmUnitOfPresentation:
    pass

@csv_mapping(
    code=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['code'])),
    display=AttributeInfo(set_value=lambda x: string_or_none(x['display'])),
)
class EdqmRouteOfAdministration:
    pass

@csv_mapping(
    ingredientCode=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['ingredient_code'])),
    moiety=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['moiety'])),
    modifier=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['d_modifier'])),
)
class SubstanceWithRolePai:
    pass

@csv_mapping(
    referenceNumeratorValue=AttributeInfo(set_value=lambda x: int_or_none(x['referenceNumeratorValue'])),
    referenceNumeratorUnit=AttributeInfo(set_value=lambda x: string_or_none(x['referenceNumeratorUnit'])),
    referenceDenominatorValue=AttributeInfo(set_value=lambda x: int_or_none(x['referenceDenominatorValue'])),
    referenceDenominatorUnit=AttributeInfo(set_value=lambda x: string_or_none(x['referenceDenominatorUnit'])),
    numeratorValue=AttributeInfo(set_value=lambda x: int_or_none(x['numeratorValue'])),
    numeratorUnit=AttributeInfo(set_value=lambda x: string_or_none(x['numeratorUnit'])),
    denominatorValue=AttributeInfo(set_value=lambda x: int_or_none(x['denominatorValue'])),
    denominatorUnit=AttributeInfo(set_value=lambda x: string_or_none(x['denominatorUnit'])),
    isPresentationStrength=AttributeInfo(set_value=lambda x: string_or_none(x['isPresentationStrength'])),
)
class Strength:
    pass

@csv_mapping(
    phpId=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['PhPID_MD5'])),
    preciseActiveIngredient=AttributeInfo(set_value=lambda x: SubstanceWithRolePai(x)),
    pharmaceuticalDoseForm=AttributeInfo(set_value=lambda x: EdqmDoseForm({'code': x['pdf_code'], 'display': x['english_pdf_and_mdf']})),
    normalizedStrength=AttributeInfo(set_value=lambda x: Strength({
        'referenceNumeratorValue': x['strenght_nominator_value_low_limit'],    
        'referenceNumeratorUnit': x['strength_nominator_unit'],
        'referenceDenominatorValue': None,
        'referenceDenominatorUnit': None,
        'numeratorValue': None,
        'numeratorUnit': None,
        'denominatorValue': None,
        'denominatorUnit': None,
        'isPresentationStrength': True,
    })),
    unitOfPresentation=AttributeInfo(set_value=lambda x: EdqmUnitOfPresentation({'code': None, 'display': None})),
)
class PharmaceuticalProduct:
    pass

@csv_mapping(
    mpId=AttributeInfo(is_key=True, set_value=lambda x: string_or_none(x['cnk_pub'])),
    country=AttributeInfo(set_value=lambda x: 'bel'),
    fullName=AttributeInfo(set_value=lambda x: string_or_none(x['medicinal_product_name'])),
    pharmaceuticalProduct=AttributeInfo(set_value=lambda x: PharmaceuticalProduct(x)),
    administrableDoseForm=AttributeInfo(set_value=lambda x: EdqmDoseForm({'code': x['adf_code'], 'display': x['english_pdf_and_mdf']})),
    unitOfPresentation=AttributeInfo(set_value=lambda x: EdqmUnitOfPresentation({'code': None, 'display': None})),
    routeOfAdminstration=AttributeInfo(set_value=lambda x: EdqmRouteOfAdministration({'code': None, 'display': x['routename_en']})),
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
