from collections import defaultdict
import numpy as np
import pandas as pd

from csvutils.csvutils import *

ORIGINA_PATH = 'original.csv'

@csv_mapping(
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
    sprimaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['substancePrimaryKey']),
    substanceCode=AttributeInfo(set_value=lambda x: x['substanceCode']),
    substanceName=AttributeInfo(set_value=lambda x: x['substanceName']),
    moietyCode=AttributeInfo(set_value=lambda x: x['moietyCode']),
    moietyName=AttributeInfo(set_value=lambda x: x['moietyName']),
    referenceStrength=AttributeInfo(set_value=lambda x: Strength({
        "concentrationNumeratorValue": x["referenceStrengthConcentrationNumeratorValue"],
        "concentrationDenominatorValue": x["referenceStrengthConcentrationDenominatorValue"],
        "concentrationNumeratorUnit": x["referenceStrengthConcentrationNumeratorUnit"],
        "concentrationDenominatorUnit": x["referenceStrengthConcentrationDenominatorUnit"],
        "presentationNumeratorValue": x["referenceStrengthPresentationNumeratorValue"],
        "presentationDenominatorValue": x["referenceStrengthPresentationDenominatorValue"],
        "presentationNumeratorUnit": x["referenceStrengthPresentationNumeratorUnit"],
        "presentationDenominatorUnit": x["referenceStrengthPresentationDenominatorUnit"],
    })),
    strength=AttributeInfo(set_value=lambda x: Strength({
        "concentrationNumeratorValue": x["strengthConcentrationNumeratorValue"],
        "concentrationDenominatorValue": x["strengthConcentrationDenominatorValue"],
        "concentrationNumeratorUnit": x["strengthConcentrationNumeratorUnit"],
        "concentrationDenominatorUnit": x["strengthConcentrationDenominatorUnit"],
        "presentationNumeratorValue": x["strengthPresentationNumeratorValue"],
        "presentationDenominatorValue": x["strengthPresentationDenominatorValue"],
        "presentationNumeratorUnit": x["strengthPresentationNumeratorUnit"],
        "presentationDenominatorUnit": x["strengthPresentationDenominatorUnit"],
    })),
)
class Substance:
    pass


@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['pharmaceuticalProductPrimaryKey']),
    administrableDoseForm=AttributeInfo(set_value=lambda x: x['administrableDoseForm']),
    pharmaceuticalProductUnitOfPresentation=AttributeInfo(set_value=lambda x: x['pharmaceuticalProductUnitOfPresentation']),
    routesOfAdministration=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(x['routesOfAdministration'])),
    ingredientRole=AttributeInfo(set_value=lambda x: x['ingredientRole']),
    substance=AttributeInfo(set_value=lambda x: Substance(x)),
)
class PharmaceuticalProduct:
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['medicinalProductPrimaryKey']),
    mpId=AttributeInfo(set_value=lambda x: x['mpId']),
    fullName=AttributeInfo(set_value=lambda x: x['fullName']),
    atcCodes=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(x['atcCodes'])),
    authorizedPharmaceuticalDoseForm=AttributeInfo(set_value=lambda x: x['authorizedPharmaceuticalDoseForm']),
    marketingAuthorizationHolder=AttributeInfo(set_value=lambda x: x['marketingAuthorizationHolder']),
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
    manufacturedItemQuantityVolumeUnit=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantityVolumeUnit']),
)
class ManufacturedItem:
    pass

@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    itemType=AttributeInfo(set_value=lambda x: x['packageItemType']),
    packageItemQuantity=AttributeInfo(set_value=lambda x: x['packageItemQuantity']),
    manufacturedItems=AttributeInfo(set_value=lambda x: [
        # we assume there is only one ManufacturedItem
        ManufacturedItem(x),
    ]),
)
class PackageItem():
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    pcId=AttributeInfo(set_value=lambda x: x['pcId']),
    packSize=AttributeInfo(set_value=lambda x: x['packSize']),
    packageItems=AttributeInfo(set_value=lambda x: [
        # we assume there is only one PackageItem
        PackageItem(x),
    ]),
    medicinalProduct=AttributeInfo(set_value=lambda x: MedicinalProduct(x))
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')    
    df = pd.read_csv(ORIGINA_PATH, keep_default_na=False, dtype=str)
    df = df.replace(r'^\s*$', None, regex=True)
    df = df.astype(dtype={
        'packSize': int,
        'packageItemQuantity': int,
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

    packages_list = []

    for index, x in df.iterrows():
        packages_list.append(PackagedMedicinalProduct(x))
        pass

    print(packages_list)
