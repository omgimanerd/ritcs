#!/usr/bin/env python3

import calendar
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.colors as mclr
import matplotlib.cm as cm
import numpy as np

from sklearn.cluster import KMeans
from sklearn.cluster import DBSCAN

from data import load_data

EPSILON = 0.15
KM_PER_RADIAN = 7000

MIN_LONGITUDE = -74.2
MAX_LONGITUDE = -73.5
MIN_LATITUDE = 40.4
MAX_LATITUDE = 41.2

def visualize_by_weekday(data, title=None):
    fig, ax = plt.subplots()
    if title:
        plt.title(title)
    plt.xlabel('Day of the Week')
    plt.ylabel('Frequency')
    plt.xlim(-0.5, 6.5)
    plt.hist(data['DATE'].dt.weekday, bins=np.arange(8) - 0.5, rwidth=0.5)
    plt.axis('tight')
    plt.gcf().set_size_inches(10, 10)
    plt.xticks(np.arange(7), calendar.day_name, rotation=45)
    plt.show()

def visualize_by_comparative_weekday(data1, data2, label, title=None):
    fig, ax = plt.subplots()
    if title:
        plt.title(title)
    plt.xlabel('Day of the Week')
    plt.ylabel('Frequency')
    plt.xlim(-0.5, 6.5)
    plt.hist([data1['DATE'].dt.weekday, data2['DATE'].dt.weekday],
             bins=np.arange(8) - 0.5, rwidth=0.5, label=label)
    plt.legend(loc='upper right')
    plt.axis('tight')
    plt.gcf().set_size_inches(10, 10)
    plt.xticks(np.arange(7), calendar.day_name, rotation=45)
    plt.show()

def visualize_by_time(data, title=None):
    fig, ax = plt.subplots()
    if title:
        plt.title(title)
    plt.xlabel('Time of Day')
    plt.ylabel('Frequency')
    plt.xlim(-0.5, 23.5)
    plt.hist(data['TIME'].dt.hour, bins=np.arange(25) - 0.5, rwidth=0.5)
    plt.axis('tight')
    plt.gcf().set_size_inches(10, 10)
    plt.xticks(np.arange(24))
    plt.show()

def visualize_by_borough(data, title=None):
    fig, ax = plt.subplots()
    if title:
        plt.title(title)
    plt.xlabel('Borough')
    plt.ylabel('Frequency')
    boroughs = ['MANHATTAN', 'BROOKLYN', 'BRONX', 'QUEENS', 'STATEN ISLAND']
    frequencies = [(data['BOROUGH'] == borough).sum() for borough in boroughs]
    plt.bar(boroughs, frequencies)
    plt.axis('tight')
    plt.gcf().set_size_inches(10, 10)
    plt.show()

def plot_kmeans_sse(data):
    fig, ax = plt.subplots()
    latlon = data[['LATITUDE', 'LONGITUDE']]
    sse = []
    for k in np.arange(2, 10):
        kmeans = KMeans(n_clusters=k, random_state=0).fit(latlon)
        sse.append(kmeans.inertia_)
    plt.title('SSE for K-means Clustering')
    plt.xlabel('k chosen')
    plt.ylabel('Sum of Squared Error')
    plt.axis('tight')
    plt.plot(np.arange(2, 10), sse)
    plt.show()

def get_basemap(ax, resolution='f'):
    from mpl_toolkits.basemap import Basemap
    m = Basemap(ax=ax, projection='merc',
                llcrnrlon=MIN_LONGITUDE,
                urcrnrlon=MAX_LONGITUDE,
                llcrnrlat=MIN_LATITUDE,
                urcrnrlat=MAX_LATITUDE,
                resolution=resolution)
    m.drawcoastlines()
    m.drawstates(linestyle='dotted')
    return m

def visualize_kmeans(data, k=7, title=None):
    fig, ax = plt.subplots()
    m = get_basemap(ax)
    # Assume locally Euclidean
    kmeans = KMeans(n_clusters=k, random_state=0).fit(
        data[['LONGITUDE', 'LATITUDE']].values)
    x, y = m(data['LONGITUDE'].values, data['LATITUDE'].values)
    for i, color in enumerate('bgrcmyk'):
        cluster = kmeans.labels_ == i
        cx, cy = x[cluster], y[cluster]
        m.scatter(cx, cy, marker='o', color=color)
    if title:
        plt.title(title)
    plt.axis('tight')
    plt.gcf().set_size_inches(15, 10)
    plt.show()

def visualize_dbscan(data, title=None):
    data = data.sample(frac=0.01)
    fig, ax = plt.subplots()
    m = get_basemap(ax)
    # Adapted from
    # https://geoffboeing.com/2014/08/clustering-to-reduce-spatial-data-set-size
    db = DBSCAN(eps=5 / KM_PER_RADIAN,
                min_samples=100, algorithm='ball_tree',
                metric='haversine').fit(
                    np.radians(data[['LONGITUDE', 'LATITUDE']].values))
    colors = cm.rainbow(np.linspace(0, 1, len(db.labels_)))
    x, y = m(data['LONGITUDE'].values, data['LATITUDE'].values)
    for i, color in enumerate(colors):
        cluster = db.labels_ == i
        cx, cy = x[cluster], y[cluster]
        m.scatter(cx, cy, marker='o', c=[color])
    if title:
        plt.title(title)
    plt.gcf().set_size_inches(15, 10)
    plt.show()

def visualize_heatmap(data, n_lat_bins=60, n_lon_bins=60, title=None):
    # Adapted from http://bagrow.com/dsv/heatmap_basemap.html
    lon, lat = data['LONGITUDE'], data['LATITUDE']
    fig, ax = plt.subplots()
    m = get_basemap(ax)
    lon_bins = np.linspace(
        min(lon) - EPSILON, max(lon) + EPSILON, n_lon_bins + 1)
    lat_bins = np.linspace(
        min(lat) - EPSILON, max(lat) + EPSILON, n_lon_bins + 1)
    density, _, _ = np.histogram2d(lat, lon, [lat_bins, lon_bins])
    x, y = m(*np.meshgrid(lon_bins, lat_bins))
    cdict = {
        'red': ( (0.0,  1.0,  1.0), (1.0,  0.9,  1.0) ),
        'green': ( (0.0,  1.0,  1.0), (1.0,  0.03, 0.0) ),
        'blue': ( (0.0,  1.0,  1.0), (1.0,  0.16, 0.0) )
    }
    if title:
        plt.title(title)
    plt.register_cmap(cmap=mclr.LinearSegmentedColormap('custom_map', cdict))
    plt.pcolormesh(x, y, density, cmap='custom_map')
    plt.axis('tight')
    plt.gcf().set_size_inches(10, 10)
    plt.show()

def calculate_cross_correlation_of_times(first_times, second_times):
    plt.xcorr(first_times[0].astype(np.float32),
              second_times[0].astype(np.float32))
    plt.show()

def main():
    data2017 = load_data('data_2017.csv')
    data2018 = load_data('data_2018.csv')
    june2017 = data2017.loc[data2017['DATE'].dt.month_name() == 'June']
    july2017 = data2017.loc[data2017['DATE'].dt.month_name() == 'July']
    june2018 = data2018.loc[data2018['DATE'].dt.month_name() == 'June']
    july2018 = data2018.loc[data2018['DATE'].dt.month_name() == 'July']
    brooklyn2018 = data2018.loc[data2018['BOROUGH'] == 'BROOKLYN']

    times_2017 = np.histogram(data2017['TIME'].dt.hour, bins=np.arange(23))
    times_2018 = np.histogram(data2018['TIME'].dt.hour, bins=np.arange(23))
    calculate_cross_correlation_of_times(times_2017, times_2018)

    # Clustering of accident frequency
    visualize_kmeans(data2017, title='KMeans clustering for 2017')
    visualize_dbscan(data2017, title='DBScan clustering for 2017')
    visualize_dbscan(brooklyn2018, title='DBScan clustering for 2018')

    # Heatmaps of accident frequency
    visualize_heatmap(data2017, title='Accident Frequency in 2017')
    visualize_heatmap(june2017, title='Accident Frequency in June 2017')
    visualize_heatmap(july2017, title='Accident Frequency in July 2017')
    visualize_heatmap(brooklyn2018,
                      n_lat_bins=120,
                      n_lon_bins=120,
                      title='Accident Frequency in Brooklyn 2017')

    # Histogram by borough
    visualize_by_borough(data2018, title='Accidents by Borough in 2018')

    # Temporal visualizations
    visualize_by_weekday(data2017, title='Accidents by Weekday in 2017')
    visualize_by_weekday(june2017, title='Accidents by Weekday in June 2017')
    visualize_by_weekday(july2017, title='Accidents by Weekday in July 2017')
    visualize_by_weekday(data2018, title='Accidents by Weekday in 2018')
    visualize_by_weekday(june2018, title='Accidents by Weekday in June 2018')
    visualize_by_weekday(july2018, title='Accidents by Weekday in July 2018')

    visualize_by_time(data2017, title='Accidents by Time in 2017')
    visualize_by_time(june2017, title='Accidents by Time in June 2017')
    visualize_by_time(july2017, title='Accidents by Time in July 2017')
    visualize_by_time(data2018, title='Accidents by Time in 2018')
    visualize_by_time(june2018, title='Accidents by Time in June 2018')
    visualize_by_time(july2018, title='Accidents by Time in July 2018')

    visualize_by_comparative_weekday(june2017, july2017,
                                     ['June 2017', 'July 2017'],
                                     title='June 2017 vs July 2017')
    visualize_by_comparative_weekday(june2018, july2018,
                                     ['June 2018', 'July 2018'],
                                     title='June 2018 vs July 2018')

    weekdays2018 = data2018.loc[data2018['DATE'].dt.weekday < 5]
    weekends2018 = data2018.loc[data2018['DATE'].dt.weekday >= 5]
    visualize_by_time(weekends2018,
                      title='Accidents by Time during Weekends in 2018')
    visualize_by_time(weekdays2018,
                      title='Accidents by Time during Weekdays in 2018')

if __name__ == '__main__':
    main()
