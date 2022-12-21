from collections import defaultdict
import numpy as np
import pandas as pd
import sys
import argparse

from csvutils.csvutils import *

ORIGINA_PATH = 'original.csv'

def int_or_none(int_obj):
    try:
        return int(int_obj)
    except:
        return None

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['primaryKey']),
    concentrationNumeratorValue=AttributeInfo(set_value=lambda x: x['concentrationNumeratorValue']),
    concentrationDenominatorValue=AttributeInfo(set_value=lambda x: x['concentrationDenominatorValue']),
    concentrationNumeratorUnit=AttributeInfo(set_value=lambda x: x['concentrationNumeratorUnit']),
    concentrationDenominatorUnit=AttributeInfo(set_value=lambda x: x['concentrationDenominatorUnit']),
    presentationNumeratorValue=AttributeInfo(set_value=lambda x: x['presentationNumeratorValue']),
    presentationDenominatorValue=AttributeInfo(set_value=lambda x: x['presentationDenominatorValue']),
    presentationNumeratorUnit=AttributeInfo(set_value=lambda x: x['presentationNumeratorUnit']),
    presentationDenominatorUnit=AttributeInfo(set_value=lambda x: x['presentationDenominatorUnit']),
)
class Strength:
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['substancePrimaryKey']),
    substanceCode=AttributeInfo(set_value=lambda x: x['substanceCode']),
    # substanceName=AttributeInfo(set_value=lambda x: x['substanceName']),
    # moietyCode=AttributeInfo(set_value=lambda x: x['moietyCode']),
    # moietyName=AttributeInfo(set_value=lambda x: x['moietyName']),
)
class Substance:
    pass


@csv_mapping(
    # Assume only one Ingredient per PharmaceuticalProduct
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['pharmaceuticalProductPrimaryKey']),
    role=AttributeInfo(set_value=lambda x: '100000072072'), # assume it's Active (spor)
    substance=AttributeInfo(set_value=lambda x: Substance(x)),
    referenceStrength=AttributeInfo(set_value=lambda x: Strength({
        'primaryKey': 'referenceStrength-' + x['pharmaceuticalProductPrimaryKey'],
        'concentrationNumeratorValue': x['referenceStrengthConcentrationNumeratorValue'],
        'concentrationDenominatorValue': x['referenceStrengthConcentrationDenominatorValue'],
        'concentrationNumeratorUnit': x['referenceStrengthConcentrationNumeratorUnit'],
        'concentrationDenominatorUnit': x['referenceStrengthConcentrationDenominatorUnit'],
        'presentationNumeratorValue': x['referenceStrengthPresentationNumeratorValue'],
        'presentationDenominatorValue': x['referenceStrengthPresentationDenominatorValue'],
        'presentationNumeratorUnit': x['referenceStrengthPresentationNumeratorUnit'],
        'presentationDenominatorUnit': x['referenceStrengthPresentationDenominatorUnit'],
    })),
    strength=AttributeInfo(set_value=lambda x: Strength({
        'primaryKey': 'strength-' + x['pharmaceuticalProductPrimaryKey'],
        'concentrationNumeratorValue': x['strengthConcentrationNumeratorValue'],
        'concentrationDenominatorValue': x['strengthConcentrationDenominatorValue'],
        'concentrationNumeratorUnit': x['strengthConcentrationNumeratorUnit'],
        'concentrationDenominatorUnit': x['strengthConcentrationDenominatorUnit'],
        'presentationNumeratorValue': x['strengthPresentationNumeratorValue'],
        'presentationDenominatorValue': x['strengthPresentationDenominatorValue'],
        'presentationNumeratorUnit': x['strengthPresentationNumeratorUnit'],
        'presentationDenominatorUnit': x['strengthPresentationDenominatorUnit'],
    })),
)
class Ingredient:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['pharmaceuticalProductPrimaryKey']),
    administrableDoseForm=AttributeInfo(set_value=lambda x: x['administrableDoseForm']),
    unitOfPresentation=AttributeInfo(set_value=lambda x: x['pharmaceuticalProductUnitOfPresentation']),
    routesOfAdministration=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(x['routesOfAdministration'])),
)
class PharmaceuticalProduct:
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['medicinalProductPrimaryKey']),
    mpId=AttributeInfo(set_value=lambda x: x['mpId']),
    fullName=AttributeInfo(set_value=lambda x: x['fullName']),
    atcCodes=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(x['atcCodes'])),
    authorizedPharmaceuticalDoseForm=AttributeInfo(set_value=lambda x: x['authorizedPharmaceuticalDoseForm']),
    marketingAuthorizationHolderCode=AttributeInfo(set_value=lambda x: x['marketingAuthorizationHolder']),
    marketingAuthorizationHolderLabel=AttributeInfo(set_value=lambda x: x['marketingAuthorizationHolderLabel']),
    country=AttributeInfo(set_value=lambda x: x['country']),
    pharmaceuticalProduct=AttributeInfo(set_value=lambda x: PharmaceuticalProduct(x)),
)
class MedicinalProduct:
    pass



@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge, and only one ManufacturedItem per PackageItem)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    manufacturedDoseForm=AttributeInfo(set_value=lambda x: x['manufacturedDoseForm']),
    unitOfPresentation=AttributeInfo(set_value=lambda x: x['manufacturedItemUnitOfPresentation']),
    manufacturedItemQuantity=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantity']),
    volumeUnit=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantityVolumeUnit']),
    ingredients=AttributeInfo(set_value=lambda x: [
        # Assume only one Ingredient
        Ingredient(x)
    ]),
)
class ManufacturedItem:
    pass

@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    type=AttributeInfo(set_value=lambda x: x['packageItemType']),
    packageItemQuantity=AttributeInfo(set_value=lambda x: int_or_none(x['packageItemQuantity'])),
    manufacturedItems=AttributeInfo(set_value=lambda x: [
        # we assume there is only one ManufacturedItem
        ManufacturedItem(x),
    ]),
    parentPackageItem=AttributeInfo(set_value=lambda x: None),
    childrenPackageItems=AttributeInfo(set_value=lambda x: []),
)
class PackageItem():
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    pcId=AttributeInfo(set_value=lambda x: x['pcId']),
    packSize=AttributeInfo(set_value=lambda x: int_or_none(x['packSize'])),
    packageItems=AttributeInfo(set_value=lambda x: [
        # we assume there is only one PackageItem
        PackageItem(x),
    ]),
    medicinalProduct=AttributeInfo(set_value=lambda x: MedicinalProduct(x))
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--color', action='store_true', help='If true the ouput will contain ANSI colors')
    args = parser.parse_args()

    df = pd.read_csv(ORIGINA_PATH, keep_default_na=False, dtype=str)
    df = df.replace(r'^\s*$', None, regex=True)
    df = df.replace(r'^NULL$', None, regex=True)

    # Notes for Robert
    # 1. Column named 'FullName' instead of 'fullName'
    # > rename column

    # 2. 'mL' inside column 'referenceStrengthPresentationDenominatorValue
    # > replace with None
    df['referenceStrengthPresentationDenominatorValue'] = df['referenceStrengthPresentationDenominatorValue'].replace('mL', None)

    # 3. 'packSize' should be an integer, not '20 x 100 ml', or 'ml'
    # > set wrong values of 'packSize' to None
    df['packSize'] = df['packSize'].replace(r'[^0-9]+', None, regex=True)

    # 4. 'pcId' column is missing
    # > add empty column for 'pcId'
    df['pcId'] = None

    # 7. typo: 'authorisedPharmaceuticalDoseForm' instead of 'authorizedPharmaceuticalDoseForm'
    # > fix typo
    df['authorizedPharmaceuticalDoseForm'] = df['authorisedPharmaceuticalDoseForm']

    df = df.astype(dtype={
        'packSize': 'Int64',
        'packageItemQuantity': 'Int64',
        'manufacturedItemQuantity': float,
        'referenceStrengthConcentrationNumeratorValue': float,
        'referenceStrengthConcentrationDenominatorValue': float,
        'referenceStrengthPresentationNumeratorValue': float,
        'referenceStrengthPresentationDenominatorValue': float,
        'strengthConcentrationNumeratorValue': float,
        'strengthConcentrationDenominatorValue': float,
        'strengthPresentationNumeratorValue': float,
        'strengthPresentationDenominatorValue': float,
    })
    df = df.replace(np.nan, None)

    df['country'] = df['country'].str.lower()

    packages_list = []
    conflic_list = []
    for index, x in df.iterrows():
        try:
            packages_list.append(PackagedMedicinalProduct(x))
        except ConflictException as e:
            if e.key != (None,):
                print(f"row {index}: {e.get_message(color=args.color)}", file=sys.stderr)
                conflic_list.append(e)
        pass
    
    if len(conflic_list) > 0:
        print(f'found {len(conflic_list)} conflicts', file=sys.stderr)

    print(packages_list)
