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
    administrableDoseForm=AttributeInfo(set_value=lambda x: EdqmDoseForm({'code': x['adf_code'], 'display': x['adf_term']})),
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
    packages_conflicts = []

    medicinal_products = {}
    medicinal_products_conflicts = []
    
    pharmaceutical_products = {}
    pharmaceutical_products_conflicts = []

    def print_results(dct, cls):
        print(f'\n\nFound {len(dct)} instances of {cls.__name__}')
        for key, val in dct.items():
            print(f'{key}: {val}\n')
        pass
    
    def dict_insert_eq_check(my_dict, entry, line_number):
        key = entry.get_key()
        if key in my_dict:
            if my_dict[key] != entry:
                raise ValueError(
                    f'-------------------\
                    \nConflicting values for key {key}:\
                    \nnew entry: {entry}\
                    \nold entry: {my_dict[key]}\
                    \n----------------\n'
                )
            return
        my_dict[key] = entry

    with open(ORIGINA_PATH, 'r') as csvin:
        csvreader = csv.DictReader(csvin, delimiter=',')

        line_number = 0
        for row in csvreader:
            line_number += 1

            try:
                dict_insert_eq_check(pharmaceutical_products, PharmaceuticalProduct(row), line_number)
            except Exception as e:
                print(e)
                pharmaceutical_products_conflicts.append(line_number)

            try:
                dict_insert_eq_check(medicinal_products, MedicinalProduct(row), line_number)
            except Exception as e:
                print(e)
                medicinal_products_conflicts.append(line_number)

            try:              
                dict_insert_eq_check(packages, PackagedMedicinalProduct(row), line_number)
            except Exception as e:
                print(e)
                packages_conflicts.append(line_number)
    
        print_results(packages, PackagedMedicinalProduct)

        print(
            f'PackagedMedicinalProducts conflicts ({len(packages_conflicts)}): {packages_conflicts}'\
            f'\nMedicinalProducts conflicts ({len(medicinal_products_conflicts)}): {medicinal_products_conflicts}'\
            f'\nPharmaceuticalProducts conflicts ({len(pharmaceutical_products_conflicts)}): {pharmaceutical_products_conflicts}'
        )


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
