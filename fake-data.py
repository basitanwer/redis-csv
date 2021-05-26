from locale import format_string
from faker import Faker
fake = Faker()
index = 0
with open('fake-data.txt', 'w', encoding='utf-8') as compiled:
    lines = []
    for index in range(0, 10_000_000):
        profile = fake.simple_profile()
        line = f"HSET fake:{index} "
        for key in profile:
            value = profile[key]
            if key == 'birthdate':
                value = profile['birthdate'].isoformat()
            value = value.replace('\n', '')
            line = line + f" {key} \"{value}\""
        height = fake.pyfloat(left_digits=1, right_digits=2, positive=True, min_value=5, max_value=9)
        line = line + f" height  {height} \n"
        lines.append(line)
        if index % 1000 == 0:
            print(index)
            compiled.writelines(lines)
            lines = []
    compiled.writelines(lines)
