#!/usr/bin/env python3

from xml.etree import cElementTree as ElementTree

import random
import time

STREET_SUFFIXES = ['RD', 'ST', 'BLVD', 'AVE', 'LN', 'DR']
STATES = ['NY', 'NH', 'MA', 'PA', 'NJ', 'VT', 'ME', 'OH',
          'IN', 'IL', 'RI', 'CT']
ENGINES = ['V8', 'V6', 'W', 'Inine', 'Electric', 'Diesel', 'Petrol']
COLORS = ['Dark Blue', 'Red', 'Beige', 'White', 'Black', 'Grey',
          'Dark Grey', 'Blue', 'Green', 'Orange', 'Yellow', 'Purple',
          'Pearl', 'Dark Green', 'Light Grey']
TRANSMISSIONS = ['Standard', 'Automatic']

AUTO_ID_UPDATER = '''
select setval('customers_id_seq', (select max(id) from customers) + 1);
select setval('dealers_id_seq', (select max(id) from dealers) + 1);
select setval('sales_id_seq', (select max(id) from sales) + 1);
'''

def chance(p=0.5):
    return random.random() < p

def postprocess_value(v):
    if type(v) is int:
        return str(v)
    elif type(v) is str:
        return '\'{}\''.format(v)
    elif v is None:
        return 'null'
    else:
        print(type(v), v)
        raise ValueError('fuck')

def generate_sql(tablename, data):
    assert len(data) != 0
    keys = data[0].keys()
    columns = ', '.join(map(str, keys))
    sql = []
    for row in data:
        values = ', '.join([postprocess_value(row[key]) for key in keys])
        sql.append('insert into {}({}) values({});'.format(
            tablename, columns, values))
    return '\n'.join(sql)

if __name__ == '__main__':
    random.seed(0)
    with open('data/male.txt') as f:
        male = [line.strip().split(' ')[0] for line in f]
    with open('data/female.txt') as f:
        female = [line.strip().split(' ')[0] for line in f]
    names = male + female

    cars_file = ElementTree.parse('data/cars.xml')
    brand_models = {}
    for car in cars_file.getroot().iter('car'):
        car_name = car[0].text
        brand_models[car_name] = [model.text for model in car[1]]

    customers = []
    for i in range(500):
        if chance():
            name = random.choice(male)
            gender = 'Male'
        else:
            name = random.choice(female)
            gender = 'Female'
        customers.append({
            'id': i,
            'name': name,
            'phone': random.randint(1000000000, 9999999999),
            'gender': gender,
            'income': random.randint(30000, 1000000),
            'address_street': '{} {} {}'.format(
                random.randint(100, 9999), random.choice(names),
                random.choice(STREET_SUFFIXES)
            ),
            'address_state': random.choice(STATES),
            'address_zipcode': random.randint(10000, 99999)
        })

    brands = []
    with open('data/brands.csv') as f:
        for line in f:
            name, country, reliability = line.strip().replace(
                '\'', '').split(',')
            brands.append({
                'name': name,
                'country': country,
                'reliability': reliability
            })

    dealers = []
    for i in range(30):
        dealers.append({
            'id': i,
            'name': random.choice(names),
            'phone': random.randint(1000000000, 9999999999)
        })

    brand_dealer = []
    for dealer in dealers:
        brand_dealer += [{
            'dealer': dealer['id'],
            'brand': brand['name']
        } for brand in random.sample(brands, random.randint(3, 12))]

    sales = []
    for i in range(1000):
        b_time = time.mktime(time.strptime('1/1/2015', '%m/%d/%Y'))
        e_time = time.mktime(time.strptime('12/31/2017', '%m/%d/%Y'))
        n_time = time.localtime(random.randint(b_time, e_time))
        timestring = time.strftime('%m/%d/%Y', n_time)
        sales.append({
            'id': i,
            'close_date': timestring,
            'customer': random.choice(customers)['id'],
            'dealer': random.choice(dealers)['id']
        })

    vehicles = []
    for sale in sales:
        dealer = random.choice(dealers)['id']
        owner = random.choice(customers)['id']
        for i in range(random.randint(1, 4)):
            brand = random.choice(brands)['name']
            vehicles.append({
                'color': random.choice(COLORS),
                'brand': brand,
                'model': random.choice(brand_models[brand]),
                'engine': random.choice(ENGINES),
                'transmission': random.choice(TRANSMISSIONS),
                'mileage': random.randint(0, 75000),
                'price': random.randint(1000, 150000),
                'sale': sale['id'],
                'dealer': dealer,
                'owner': owner
            })
    for i in range(len(vehicles) // 5):
        brand = random.choice(brands)['name']
        vehicles.append({
            'color': random.choice(COLORS),
            'brand': brand,
            'model': random.choice(brand_models[brand]),
            'engine': random.choice(ENGINES),
            'transmission': random.choice(TRANSMISSIONS),
            'mileage': random.randint(0, 75000),
            'price': random.randint(1000, 150000),
            'sale': None,
            'dealer': random.choice(dealers)['id'],
            'owner': None
        })
    random.shuffle(vehicles)
    for i, vehicle in enumerate(vehicles):
        vehicle['vin'] = i

    with open('populate.sql', 'w') as f:
        f.write(generate_sql('customers', customers))
        f.write(generate_sql('brands', brands))
        f.write(generate_sql('dealers', dealers))
        f.write(generate_sql('brand_dealer', brand_dealer))
        f.write(generate_sql('sales', sales))
        f.write(generate_sql('vehicles', vehicles))
        f.write(AUTO_ID_UPDATER)
