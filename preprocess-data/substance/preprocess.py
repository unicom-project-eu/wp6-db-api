import csv
import re

ORIGINA_PATH = "original.csv"
OUTPUT_PATH = "substances_pai.csv"

if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')

    output_rows = []

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

            output_rows.append({
                'moiety': moiety,
                'modifier': modifier,
                'ingredient_code': ingredient_code,
            })
        pass

    print(f'Writing output to "{OUTPUT_PATH}"...')

    with open(OUTPUT_PATH, 'w') as csvout:
        csvwriter = csv.DictWriter(csvout, list((output_rows[0].keys())))
        csvwriter.writeheader()
        csvwriter.writerows(output_rows)
    
    print('Done')
