from collections import defaultdict
import pandas as pd

from csvutils.csvutils import *

def mocked_value(val):
    return f'MOCK-{val}'

ORIGINA_PATH = 'original.csv'

@csv_mapping(
    medicinalProductPrimaryKey=AttributeInfo(is_key=True, set_value=lambda x: x['medicinalProductPrimaryKey']),
    mpId=AttributeInfo(set_value=lambda x: x['mpId']),
    fullName=AttributeInfo(set_value=lambda x: x['mpId']),
    atcCodes=AttributeInfo(set_value=lambda x: comma_separated_str_to_list(str(x['mpId']))),
)
class MedicinalProduct:
    pass


@csv_mapping(
    packagedMedicinalProductPrimaryKey=AttributeInfo(is_key=True, set_value=lambda x: x['packagedMedicinalProductPrimaryKey']),
    pcId=AttributeInfo(set_value=lambda x: x['pcId']),
    packSize=AttributeInfo(set_value=lambda x: x['packSize']),
    packageItemType=AttributeInfo(set_value=lambda x: x['packageItemType']),
    manufacturedDoseForm=AttributeInfo(set_value=lambda x: x['manufacturedDoseForm']),
    manufacturedItemUnitOfPresentation=AttributeInfo(set_value=lambda x: x['manufacturedItemUnitOfPresentation']),
    manufacturedItemQuantity=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantity']),
    manufacturedItemQuantityVolumeUnit=AttributeInfo(set_value=lambda x: x['manufacturedItemQuantityVolumeUnit']),
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

    packages_list = []

    for index, x in df.iterrows():
        packages_list.append(PackagedMedicinalProduct(x))
        pass

    print(packages_list)
