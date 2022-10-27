from pathlib import Path
from urllib.parse import urlparse
import jsonschema
import requests


import json
import jsonschema
import argparse



if __name__ == '__main__':
    arg_parser = argparse.ArgumentParser(
        prog='Json import validator',
    )

    arg_parser.add_argument('input_file', type=Path, help='Json file to validate')
    arg_parser.add_argument('schema_url')
    args = arg_parser.parse_args()

    print(args.schema_url)

    with open(args.input_file, 'r') as input_file:
        obj = json.load(input_file)
        pass

    response = requests.get(args.schema_url)
    schema = response.json()
    
    jsonschema.validate(obj, schema=schema)

    pass