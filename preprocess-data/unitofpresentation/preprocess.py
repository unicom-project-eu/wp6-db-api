import csv
from bs4 import BeautifulSoup
import re

ORIGINA_PATH = "original.html"
OUTPUT_PATH = "unit_of_presentation.csv"

if __name__ == '__main__':
    print(f'Parsing "{ORIGINA_PATH}"...')

    with open(ORIGINA_PATH, 'r') as origina_file:
        html = origina_file.read()

    soup = BeautifulSoup(html, features="html5lib")
    
    table = soup.find('table')
    headers_text = list(map(lambda th: re.sub(r'\s+', '_', th.string.lower().strip()), table.find_all('th')))

    input_rows = []
    for tr in table.find_all('tr')[1:]:
        tds = tr.find_all('td')
        input_rows.append({
            headers_text[i]: tds[i].string
            for i in range(len(headers_text))
        })
    
    print(f'Writing output to "{OUTPUT_PATH}"...')

    with open(OUTPUT_PATH, 'w') as csvout:
        out_headers_map = {
            'expanded_code': 'code',
            'term': 'display',
        }

        csvwriter = csv.DictWriter(csvout, out_headers_map.values())
        csvwriter.writeheader()

        for row in input_rows:
            csvwriter.writerow({
                out_headers_map[k]: v
                for k,v in row.items()
                if k in out_headers_map
            })

    print('Done')
