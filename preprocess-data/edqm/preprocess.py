from os import listdir
from os.path import isfile, join

import os
import pandas as pd

ORIGINA_DIR = './original'
OUT_DIR = './processed'

def json_to_csv(f):
    df = pd.read_json(join(ORIGINA_DIR, f))
    df = df.rename(columns={'isocode': 'code'})

    df = df[['code', 'term', 'definition', 'comment']]

    df['code'] = df['code'].apply(lambda x: x.split('-')[1])

    df.to_csv(join(OUT_DIR, f.replace('.json', '.csv')), index = None)


if __name__ == '__main__':
    for f in listdir(ORIGINA_DIR):
        if not isfile(join(ORIGINA_DIR, f)):
            continue

        json_to_csv(f)
    pass