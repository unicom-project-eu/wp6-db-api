import pandas as pd


if __name__ == '__main__':
    df = pd.read_csv('sms-substances-list.csv').astype({
        "#SMS_ID": str,
        "Is_Preferred_Name": bool,
    })
    
    df = df[df['Is_Preferred_Name'] == True]
    print(df)

    pass