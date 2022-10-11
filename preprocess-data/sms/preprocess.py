import pandas as pd


if __name__ == '__main__':
    df = pd.read_csv('original.csv').astype({
        "#SMS_ID": str,
        "Is_Preferred_Name": bool,
    })
    
    df = df[df['Is_Preferred_Name'] == True]
    
    df = pd.DataFrame({
        'substanceCode': df['#SMS_ID'],
        'substanceName': df['Substance_Name']
    })

    # leave empty for now
    df['moietyCode'] = None
    df['moietyName'] = None

    df['substanceName'] = df['substanceName'].apply(lambda x: x.lower())

    df.to_csv('substance.csv', index=False)
    pass