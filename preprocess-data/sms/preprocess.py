import pandas as pd


if __name__ == '__main__':
    df = pd.read_csv('original.csv').astype({
        "#SMS_ID": str,
        "Is_Preferred_Name": bool,
    })
    
    df = df[df['Is_Preferred_Name'] == True]
    
    df = pd.DataFrame({
        'substance_code': df['#SMS_ID'],
        'substance_name': df['Substance_Name']
    })

    # leave empty for now
    df['moiety_code'] = None
    df['moiety_name'] = None

    trim = lambda x: x if len(x) <= 255 else x[:252] + '...'
    df['substance_name'] = df['substance_name'].apply(lambda x: trim(x.lower()))

    df = df.drop_duplicates(subset=['substance_code'], keep='first')

    df.to_csv('substance.csv', index=False)
    pass