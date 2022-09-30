import pandas as pd

df = pd.read_json(r'./original.json')
df = df.rename(columns={'isocode': 'code'})

df['code'] = df['code'].apply(lambda x: x.split('-')[1])

df.to_csv(r'container.csv', index = None)