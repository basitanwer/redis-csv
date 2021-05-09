import pandas as pd

input = pd.read_csv("C:/test/celebrities_profiles.txt", '\t')
from datetime import datetime
from dateutil.parser import parse

columns = input.columns.values
date_column = 'created_at'

with open('compile.txt', 'w', encoding='utf-8') as compiled:
    for index, val in input.iterrows():
        line = f"HSET twitter:{index} "
        
        for name in columns:
            value = val[name]
            if name == date_column:
                value = parse(value).isoformat()
            elif type(value) != int and type(value) != float:
                value = str(value).replace('"', '').replace('\\','')
                value = f"\"{value}\""

            name = name.replace(' ', '-')
            line = line + f" {name} {value}"
        line = line + '\n'
        compiled.write(line)
