#!/usr/bin/env python3
# Constants necessary for preprocessing the data.
#
# Author: Alvin Lin (axl1439)

DURATION = 0
PROTOCOL_TYPE = 1
SERVICE = 2
FLAG = 3
SRC_BYTES = 4
DST_BYTES = 5
LAND = 6
WRONG_FRAGMENT = 7
URGENT = 8
HOT = 9
NUM_FAILED_LOGINS = 10
LOGGED_IN = 11
NUM_COMPROMISED = 12
ROOT_SHELL = 13
SU_ATTEMPTED = 14
NUM_ROOT = 15
NUM_FILE_CREATIONS = 16
NUM_SHELLS = 17
NUM_ACCESS_FILES = 18
NUM_OUTBOUND_CMDS = 19
IS_HOST_LOGIN = 20
IS_GUEST_LOGIN = 21
COUNT = 22
SRV_COUNT = 23
SERROR_RATE = 24
SRV_SERROR_RATE = 25
RERROR_RATE = 26
SRV_RERROR_RATE = 27
SAME_SRV_RATE = 28
DIFF_SRV_RATE = 29
SRV_DIFF_HOST_RATE = 30
DST_HOST_COUNT = 31
DST_HOST_SRV_COUNT = 32
DST_HOST_SAME_SRV_RATE = 33
DST_HOST_DIFF_SRV_RATE = 34
DST_HOST_SAME_SRC_PORT_RATE = 35
DST_HOST_SRV_DIFF_HOST_RATE = 36
DST_HOST_SERROR_RATE = 37
DST_HOST_SRV_SERROR_RATE = 38
DST_HOST_RERROR_RATE = 39
DST_HOST_SRV_RERROR_RATE = 40
ATTACK = 41

PROTOCOL_TYPES_MAP = {
    'udp': 0,
    'icmp': 1,
    'tcp': 2
}

PROTOCOL_TYPES_RMAP = [
    'udp', 'icmp', 'tcp'
]

SERVICE_TYPES_MAP = {
    'IRC': 0,
    'urp_i': 1,
    'courier': 2,
    'sunrpc': 3,
    'netstat': 4,
    'ecr_i': 5,
    'link': 6,
    'domain': 7,
    'imap4': 8,
    'sql_net': 9,
    'http_443': 10,
    'ftp': 11,
    'ftp_data': 12,
    'eco_i': 13,
    'ldap': 14,
    'domain_u': 15,
    'finger': 16,
    'bgp': 17,
    'other': 18,
    'name': 19,
    'login': 20,
    'smtp': 21,
    'time': 22,
    'telnet': 23,
    'ntp_u': 24,
    'private': 25,
    'systat': 26,
    'tftp_u': 27,
    'pm_dump': 28,
    'uucp': 29,
    'pop_3': 30,
    'remote_job': 31,
    'tim_i': 32,
    'pop_2': 33,
    'daytime': 34,
    'X11': 35,
    'auth': 36,
    'printer': 37,
    'http': 38,
    'nnsp': 39,
    'iso_tsap': 40,
    'echo': 41,
    'discard': 42,
    'ssh': 43,
    'whois': 44,
    'mtp': 45,
    'gopher': 46,
    'rje': 47,
    'ctf':48,
    'supdup': 49,
    'hostnames': 50,
    'csnet_ns': 51,
    'uucp_path': 52,
    'nntp': 53,
    'netbios_ns': 54,
    'netbios_dgm':55,
    'netbios_ssn': 56,
    'vmnet': 57,
    'Z39_50': 58,
    'exec': 59,
    'shell': 60,
    'efs': 61,
    'klogin': 62,
    'kshell': 63,
    'icmp': 64,
    'urh_i': 65,
    'http_2784': 66,
    'harvest': 67,
    'aol': 68,
    'http_8001': 69,
    'red_i': 70
}

SERVICE_TYPES_RMAP = [
    'IRC', 'urp_i', 'courier', 'sunrpc', 'netstat', 'ecr_i', 'link', 'domain',
    'imap4', 'sql_net', 'http_443', 'ftp', 'ftp_data', 'eco_i', 'ldap',
    'domain_u', 'finger', 'bgp', 'other', 'name', 'login', 'smtp', 'time',
    'telnet', 'ntp_u', 'private', 'systat', 'tftp_u', 'pm_dump', 'uucp',
    'pop_3', 'remote_job', 'tim_i', 'pop_2', 'daytime', 'X11', 'auth',
    'printer', 'http', 'nnsp', 'iso_tsap', 'echo', 'discard', 'ssh', 'whois',
    'mtp', 'gopher', 'rje', 'ctf', 'supdup', 'hostnames', 'csnet_ns',
    'uucp_path', 'nntp', 'netbios_ns', 'netbios_dgm', 'netbios_ssn', 'vmnet',
    'Z39_50', 'exec', 'shell', 'efs', 'klogin', 'kshell', 'icmp', 'urh_i',
    'http_2784', 'harvest', 'aol', 'http_8001', 'red_i'
]

FLAG_TYPES_MAP = {
    'S2': 0,
    'RSTR': 1,
    'SH': 2,
    'S1': 3,
    'RSTOS0': 4,
    'SF': 5,
    'S3': 6,
    'S0': 7,
    'REJ': 8,
    'RSTO': 9,
    'OTH': 10
}

FLAG_TYPES_RMAP = [
    'S2', 'RSTR', 'SH', 'S1', 'RSTOS0', 'SF', 'S3', 'S0', 'REJ', 'RSTO', 'OTH'
]

ATTACK_TYPES_MAP = {
    'nmap': 0,
    'pod': 1,
    'back': 2,
    'multihop': 3,
    'guess_passwd': 4,
    'mscan': 5,
    'rootkit': 6,
    'processtable': 7,
    'saint': 8,
    'normal': 9,
    'snmpgetattack': 10,
    'loadmodule': 11,
    'apache2': 12,
    'xlock': 13,
    'phf': 14,
    'portsweep': 15,
    'xterm': 16,
    'smurf': 17,
    'udpstorm': 18,
    'ps': 19,
    'xsnoop': 20,
    'imap': 21,
    'warezmaster': 22,
    'satan': 23,
    'ipsweep': 24,
    'perl': 25,
    'named': 26,
    'neptune': 27,
    'sendmail': 28,
    'buffer_overflow': 29,
    'httptunnel' : 30,
    'worm': 31,
    'mailbomb': 32,
    'ftp_write': 33,
    'teardrop': 34,
    'land':35,
    'sqlattack': 36,
    'snmpguess': 37,
    'warezclient': 38,
    'spy': 39,
}

ATTACK_TYPES_RMAP = [
    'nmap', 'pod', 'back', 'multihop', 'guess_passwd', 'mscan', 'rootkit',
    'processtable', 'saint', 'normal', 'snmpgetattack', 'loadmodule', 'apache2',
    'xlock', 'phf', 'portsweep', 'xterm', 'smurf', 'udpstorm', 'ps', 'xsnoop',
    'imap', 'warezmaster', 'satan', 'ipsweep', 'perl', 'named', 'neptune',
    'sendmail', 'buffer_overflow', 'httptunnel', 'worm', 'mailbomb',
    'ftp_write', 'teardrop', 'land', 'sqlattack', 'snmpguess', 'warezclient',
    'spy'
]
