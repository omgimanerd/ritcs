#!/usr/bin/env python3

import pandas as pd
from datetime import datetime

def preprocess_data(data):
    latitudes, longitudes = data['LATITUDE'], data['LONGITUDE']
    has_location = pd.notnull(latitudes) & pd.notnull(longitudes)
    non_zero = (latitudes != 0) & (longitudes != 0)
    non_weird = (longitudes > -75) & (longitudes < -73)
    return data.loc[has_location & non_zero & non_weird]

def load_data(filename):
    dtype = {
        'DATE': str,
        'TIME': str,
        'BOROUGH': str,
        'ZIP CODE': str,
        'LATITUDE': float,
        'LONGITUDE': float,
        'LOCATION': str,
        'ON STREET NAME': str,
        'CROSS STREET NAME': str,
        'OFF STREET NAME': str,
        'NUMBER OF PERSONS INJURED': float,
        'NUMBER OF PERSONS KILLED': float,
        'NUMBER OF PEDESTRIANS INJURED': float,
        'NUMBER OF PEDESTRIANS KILLED': float,
        'NUMBER OF CYCLIST INJURED': float ,
        'NUMBER OF CYCLIST KILLED': float,
        'NUMBER OF MOTORIST INJURED': float,
        'NUMBER OF MOTORIST KILLED': float,
        'CONTRIBUTING FACTOR VEHICLE 1': str,
        'CONTRIBUTING FACTOR VEHICLE 2': str,
        'CONTRIBUTING FACTOR VEHICLE 3': str,
        'CONTRIBUTING FACTOR VEHICLE 4': str,
        'CONTRIBUTING FACTOR VEHICLE 5': str,
        'UNIQUE KEY': int,
        'VEHICLE TYPE CODE 1': str,
        'VEHICLE TYPE CODE 2': str,
        'VEHICLE TYPE CODE 3': str,
        'VEHICLE TYPE CODE 4':str,
        'VEHICLE TYPE CODE 5': str
    }
    data = pd.read_csv(filename, dtype=dtype, parse_dates=['DATE', 'TIME'])
    return preprocess_data(data)

def main():
    data = load_data('NYPD_Motor_Vehicle_Collisions.csv')
    data.to_csv('data_preprocessed.csv')
    data.loc[data['DATE'].dt.year == 2017].to_csv('data_2017.csv')
    data.loc[data['DATE'].dt.year == 2018].to_csv('data_2018.csv')

if __name__ == '__main__':
    main()
