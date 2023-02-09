import numpy as np
import pandas as pd
import sys
import argparse

from csvutils.csvutils import *


def int_or_none(int_obj):
    try:
        return int(int_obj)
    except:
        return None


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['primaryKey']),
    concentrationNumeratorValue=AttributeInfo(set_value=lambda _x: _x['concentrationNumeratorValue']),
    concentrationDenominatorValue=AttributeInfo(set_value=lambda _x: _x['concentrationDenominatorValue']),
    concentrationNumeratorUnit=AttributeInfo(set_value=lambda _x: _x['concentrationNumeratorUnit']),
    concentrationDenominatorUnit=AttributeInfo(set_value=lambda _x: _x['concentrationDenominatorUnit']),
    presentationNumeratorValue=AttributeInfo(set_value=lambda _x: _x['presentationNumeratorValue']),
    presentationDenominatorValue=AttributeInfo(set_value=lambda _x: _x['presentationDenominatorValue']),
    presentationNumeratorUnit=AttributeInfo(set_value=lambda _x: _x['presentationNumeratorUnit']),
    presentationDenominatorUnit=AttributeInfo(set_value=lambda _x: _x['presentationDenominatorUnit']),
)
class Strength:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['substancePrimaryKey']),
    substanceCode=AttributeInfo(set_value=lambda _x: _x['substanceCode']),
    # substanceName=AttributeInfo(set_value=lambda x: x['substanceName']),
    # moietyCode=AttributeInfo(set_value=lambda x: x['moietyCode']),
    # moietyName=AttributeInfo(set_value=lambda x: x['moietyName']),
)
class Substance:
    pass


@csv_mapping(
    # Assume only one Ingredient per PharmaceuticalProduct
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['pharmaceuticalProductPrimaryKey']),
    role=AttributeInfo(set_value=lambda _x: '100000072072'),  # assume it's Active (spor)
    substance=AttributeInfo(set_value=lambda _x: Substance(_x)),
    referenceStrength=AttributeInfo(set_value=lambda _x: Strength({
        'primaryKey': 'referenceStrength-' + _x['pharmaceuticalProductPrimaryKey'],
        'concentrationNumeratorValue': _x['referenceStrengthConcentrationNumeratorValue'],
        'concentrationDenominatorValue': _x['referenceStrengthConcentrationDenominatorValue'],
        'concentrationNumeratorUnit': _x['referenceStrengthConcentrationNumeratorUnit'],
        'concentrationDenominatorUnit': _x['referenceStrengthConcentrationDenominatorUnit'],
        'presentationNumeratorValue': _x['referenceStrengthPresentationNumeratorValue'],
        'presentationDenominatorValue': _x['referenceStrengthPresentationDenominatorValue'],
        'presentationNumeratorUnit': _x['referenceStrengthPresentationNumeratorUnit'],
        'presentationDenominatorUnit': _x['referenceStrengthPresentationDenominatorUnit'],
    })),
    strength=AttributeInfo(set_value=lambda _x: Strength({
        'primaryKey': 'strength-' + _x['pharmaceuticalProductPrimaryKey'],
        'concentrationNumeratorValue': _x['strengthConcentrationNumeratorValue'],
        'concentrationDenominatorValue': _x['strengthConcentrationDenominatorValue'],
        'concentrationNumeratorUnit': _x['strengthConcentrationNumeratorUnit'],
        'concentrationDenominatorUnit': _x['strengthConcentrationDenominatorUnit'],
        'presentationNumeratorValue': _x['strengthPresentationNumeratorValue'],
        'presentationDenominatorValue': _x['strengthPresentationDenominatorValue'],
        'presentationNumeratorUnit': _x['strengthPresentationNumeratorUnit'],
        'presentationDenominatorUnit': _x['strengthPresentationDenominatorUnit'],
    })),
)
class Ingredient:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['pharmaceuticalProductPrimaryKey']),
    administrableDoseForm=AttributeInfo(set_value=lambda _x: _x['administrableDoseForm']),
    unitOfPresentation=AttributeInfo(set_value=lambda _x: _x['pharmaceuticalProductUnitOfPresentation']),
    routesOfAdministration=AttributeInfo(set_value=lambda _x: comma_separated_str_to_list(_x['routesOfAdministration'])),
)
class PharmaceuticalProduct:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['medicinalProductPrimaryKey']),
    mpId=AttributeInfo(set_value=lambda _x: _x['mpId']),
    #mpId=AttributeInfo(set_value=lambda _x: _x['mpIdLabel']),
    fullName=AttributeInfo(set_value=lambda _x: _x['fullName']),
    atcCodes=AttributeInfo(set_value=lambda _x: comma_separated_str_to_list(_x['atcCodes'])),
    authorizedPharmaceuticalDoseForm=AttributeInfo(set_value=lambda _x: _x['authorizedPharmaceuticalDoseForm']),
    marketingAuthorizationHolderCode=AttributeInfo(set_value=lambda _x: _x['marketingAuthorizationHolder']),
    marketingAuthorizationHolderLabel=AttributeInfo(set_value=lambda _x: _x['marketingAuthorizationHolderLabel']),
    country=AttributeInfo(set_value=lambda _x: _x['country']),
    pharmaceuticalProduct=AttributeInfo(set_value=lambda _x: PharmaceuticalProduct(_x)),
)
class MedicinalProduct:
    pass


@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge, and only one ManufacturedItem per PackageItem)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['packagedMedicinalProductPrimaryKey']),
    manufacturedDoseForm=AttributeInfo(set_value=lambda _x: _x['manufacturedDoseForm']),
    unitOfPresentation=AttributeInfo(set_value=lambda _x: _x['manufacturedItemUnitOfPresentation']),
    manufacturedItemQuantity=AttributeInfo(set_value=lambda _x: _x['manufacturedItemQuantity']),
    volumeUnit=AttributeInfo(set_value=lambda _x: _x['manufacturedItemQuantityVolumeUnit']),
    ingredients=AttributeInfo(set_value=lambda _x: [
        # Assume only one Ingredient
        Ingredient(_x)
    ]),
)
class ManufacturedItem:
    pass


@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['packagedMedicinalProductPrimaryKey']),
    type=AttributeInfo(set_value=lambda _x: _x['packageItemType']),
    packageItemQuantity=AttributeInfo(set_value=lambda _x: int_or_none(_x['packageItemQuantity'])),
    manufacturedItems=AttributeInfo(set_value=lambda _x: [
        # we assume there is only one ManufacturedItem
        ManufacturedItem(_x),
    ]),
    parentPackageItem=AttributeInfo(set_value=lambda _x: None),
    childrenPackageItems=AttributeInfo(set_value=lambda _x: []),
)
class PackageItem:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda _x: _x['packagedMedicinalProductPrimaryKey']),
    pcId=AttributeInfo(set_value=lambda _x: _x['pcId']),
    packSize=AttributeInfo(set_value=lambda _x: int_or_none(_x['packSize'])),
    packageItems=AttributeInfo(set_value=lambda _x: [
        # we assume there is only one PackageItem
        PackageItem(_x),
    ]),
    medicinalProduct=AttributeInfo(set_value=lambda _x: MedicinalProduct(_x))
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("input_file", type=str)
    parser.add_argument('-c', '--color', action='store_true', help='If true the ouput will contain ANSI colors')
    args = parser.parse_args()

    df = pd.read_csv(args.input_file, keep_default_na=False, dtype=str, sep=";")
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
    #df['pcId'] = None

    # 7. typo: 'authorisedPharmaceuticalDoseForm' instead of 'authorizedPharmaceuticalDoseForm'
    # > fix typo
    #df['authorizedPharmaceuticalDoseForm'] = df['authorisedPharmaceuticalDoseForm']

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
