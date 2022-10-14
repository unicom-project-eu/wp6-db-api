from collections import defaultdict
import numpy as np
import pandas as pd
import re

from csvutils.csvutils import *

ORIGINA_PATH = 'original.csv'

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
    ingredients=AttributeInfo(set_value=lambda x: [
        # Assume only one Ingredient
        Ingredient(x)
    ]),
)
class PharmaceuticalProduct:
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=False, set_value=lambda x: x['phpPrimaryKey'] + x['Medicinal Product Name'] + str(x['Package Size'])),
    # mpId=AttributeInfo(set_value=lambda x: x['mpId']),
    # fullName=AttributeInfo(set_value=lambda x: x['fullName']),
    # atcCodes=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(x['atcCodes'])),
    # authorizedPharmaceuticalDoseForm=AttributeInfo(set_value=lambda x: x['authorizedPharmaceuticalDoseForm']),
    # marketingAuthorizationHolderCode=AttributeInfo(set_value=lambda x: x['marketingAuthorizationHolder']),
    # marketingAuthorizationHolderLabel=AttributeInfo(set_value=lambda x: x['marketingAuthorizationHolderLabel']),
    # country=AttributeInfo(set_value=lambda x: x['country']),
    # pharmaceuticalProduct=AttributeInfo(set_value=lambda x: PharmaceuticalProduct(x)),
)
class MedicinalProduct:
    pass



@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge, and only one ManufacturedItem per PackageItem)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['primaryKey']),
    manufacturedDoseForm=AttributeInfo(set_value=lambda x: x['manufacturedDoseForm']),
    unitOfPresentation=AttributeInfo(set_value=lambda x: x['unitOfPresentation']),
    manufacturedItemQuantity=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantity']),
    volumeUnit=AttributeInfo(set_value=lambda x: x['volumeUnit']),
)
class ManufacturedItem:
    pass

@csv_mapping(
    # in this case we use the same primary key as packagedMedicinalProuct 
    # (we assume only one PackageItem per MedicinalProdctPacakge)
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['primaryKey']),
    type=AttributeInfo(set_value=lambda x: x['type']),
    packageItemQuantity=AttributeInfo(set_value=lambda x: x['packageItemQuantity']),
    manufacturedItems=AttributeInfo(set_value=lambda x: x['manufacturedItems']),
    childrenPackageItems=AttributeInfo(set_value=lambda x: x['childrenPackageItems']),
)
class PackageItem():
    pass

@csv_mapping(
    primaryKey=AttributeInfo(is_key=True, is_hidden=True, set_value=lambda x: x['Codice AIC - LocalMppid']),
    pcId=AttributeInfo(set_value=lambda x: None),
    packSize=AttributeInfo(set_value=lambda x: x['Package Size']),
    packageItem=AttributeInfo(set_value=lambda x: PackageItem({
        'primaryKey': 'outer' + x['Codice AIC - LocalMppid'],
        'type': '30009000',
        'packageItemQuantity': None,
        'manufacturedItems': [],
        'childrenPackageItems': [
            PackageItem({
                'primaryKey': 'inner' + x['Codice AIC - LocalMppid'],
                'type': '30007000',
                'packageItemQuantity': None,
                'manufacturedItems': [
                    ManufacturedItem({
                        'primaryKey': 'manufacturedItem1' + x['Codice AIC - LocalMppid'],
                        'manufacturedDoseForm': x['Dose form (EDQM Code)'] if x['Priorità'] <= 2 else None,
                        'unitOfPresentation': x['Unit of Presentation 1 (EDQM Code)'],
                        'manufacturedItemQuantity': x['Package Size'] if x['Priorità'] <= 2 else None,
                        'volumeUnit':  None if x['Priorità'] <= 2 else x['Active Ingredient 1 Numerator Unit of Mesurement (UCUM)'],
                    }),
                ],
                'childrenPackageItems': [],
            })
        ],
    })),
    medicinalProduct=AttributeInfo(set_value=lambda x: MedicinalProduct(x))
)
class PackagedMedicinalProduct:
    pass


if __name__ == '__main__':
    df = pd.read_csv(ORIGINA_PATH, keep_default_na=False, dtype=str)
    df = df.replace(r'^\s*$', None, regex=True)
    df = df.astype(dtype={
        'Priorità': int,
    #     'packSize': int,
    #     'packageItemQuantity': int,
    #     'manufacturedItemQuantity': float,
    #     'referenceStrengthConcentrationNumeratorValue': float,
    #     'referenceStrengthConcentrationDenominatorValue': float,
    #     'referenceStrengthPresentationNumeratorValue': float,
    #     'referenceStrengthPresentationDenominatorValue': float,
    #     'strengthConcentrationNumeratorValue': float,
    #     'strengthConcentrationDenominatorValue': float,
    #     'strengthPresentationNumeratorValue': float,
    #     'strengthPresentationDenominatorValue': float,
    })
    df = df.drop(df[df['Priorità'] > 1].index)
    df = df.replace(np.nan, None)

    # add local pharmaceuticalProductPrimaryKey
    # regex = r'Substance [0-9]+ \(SMS code\)'
    # substances_cols = [col for col in df if re.match(regex, col)]

    df['phpPrimaryKey'] = df['Principio Attivo'] + df['Dose form (EDQM Code)'] + df['Active Ingredient 1 Numerator Quantity'] + ' ' + df['Active Ingredient 1 Numerator Unit of Mesurement (UCUM)']

    packages_list = []

    for index, x in df.iterrows():
        packages_list.append(PackagedMedicinalProduct(x))
        pass

    print(packages_list)
