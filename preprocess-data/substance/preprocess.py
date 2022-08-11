from collections import defaultdict
import csv
import re

ORIGINA_PATH = "original.csv"
OUTPUT_PATH = "substances_pai.csv"

if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')

    output_rows = []
    rows_by_ingredient = defaultdict(lambda: [])

    with open(ORIGINA_PATH, 'r') as csvin:
        csvreader = csv.DictReader(csvin, delimiter=',')

        for row in csvreader:
            substance_pai_tokens = re.split(r'\s+', row['substance_pai'].lower())

            moiety = substance_pai_tokens[0]
            
            modifier = ' '.join(substance_pai_tokens[1:])
            if modifier == '':
                modifier = None

            ingredient_code = row['ingredient_code'].strip()
            if not re.match(r'^\d+$', ingredient_code):
                ingredient_code = None
            
            if ingredient_code is None:
                continue
            
            out_row = {
                'moiety': moiety,
                'modifier': modifier,
                'ingredient_code': ingredient_code,
            }

            output_rows.append(out_row)
            rows_by_ingredient[ingredient_code].append(out_row)

        pass

    print(f'Writing output to "{OUTPUT_PATH}"...')

    with open(OUTPUT_PATH, 'w') as csvout:
        csvwriter = csv.DictWriter(csvout, list((output_rows[0].keys())))
        csvwriter.writeheader()

        for row in output_rows:
            csvwriter.writerow({k: 'NULL' if v is None else v for k, v in row.items()})
    
    for code, rows in rows_by_ingredient.items():
        if len(rows) > 1:
            print(f'WARN - Duplicates for ingredient_code {code}: {rows}')

    print('Done')
