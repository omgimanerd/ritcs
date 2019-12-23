#!/usr/bin/env python3
# Author: Nathan Farrell (naf1602), Alvin Lin (axl1439)

from constants import *
from enum import Enum
from base import get_training_data, get_human_readable_row, get_validation_data
from timeit import default_timer as timer


class Result(Enum):
    TRUE_POS = 0
    TRUE_NEG = 1
    FALSE_POS = 2
    FALSE_NEG = 3


def main():
    train_data = get_training_data(100000)
    valid_data = get_validation_data(300000)

    false_positives = 0
    false_negatives = 0
    true_positive = 0
    true_negative = 0

    start = timer()
    for packet in valid_data:
        idsResult = checkPacket(packet)
        result = checkCorrectness(idsResult, packet)

        if result == Result.FALSE_POS:
            false_positives += 1
        elif result == Result.FALSE_NEG:
            false_negatives += 1
        elif result == Result.TRUE_POS:
            true_positive += 1
        elif result == Result.TRUE_NEG:
            true_negative += 1
    end = timer()

    print('True Positives: '+str(true_positive))
    print('True Negatives: '+str(true_negative))
    print('False Positive: '+str(false_positives))
    print('False Negatives: '+str(false_negatives)+'\n')
    print('Total Packets: '+str(len(valid_data))+'\n')
    print('Elapsed time: '+str(end - start)) ,



def checkCorrectness(idsResult: (str,str), packet)-> Result:
    """
    Returns whether the result of the ids is false positive/true positive/false negative/true negative
    :param idsResult:
    :return: string of whether it was false positive/true positive/false negative/true negative
    """

    if idsResult[1] == "normal" and packet[ATTACK] == 9:
        return Result.TRUE_NEG
    elif idsResult[1] == 'anomalous' and packet[ATTACK] != 9:
        return Result.TRUE_POS
    elif idsResult[1] == "normal" and packet[ATTACK] != 9:
        return Result.FALSE_NEG
    elif idsResult[1] == "anomalous" and packet[ATTACK] == 9:
        return Result.FALSE_POS


def checkPacket(packet)-> (str, str):
    """
    Uses a decision tree full of misuse rules to check the
    incoming packet and labels it anomalous vs. normal traffic.
    Also returns the name of the attack.

    :param packet: The "incoming" packet
    :return: (attack_name, anomalous vs. normal)
    """
    duration = packet[0]
    protocol = packet[1]
    service = packet[2]
    flag = packet[3]
    src_bytes = packet[4]
    dst_bytes = packet[5]
    dst_host_srv_err_rate = packet[39]
    dst_home_same_src_port_rate = packet[35]

    # First layer: Protocol

    # UDP
    if protocol == 0:

        # Second layer: Service
        if service == 25: # private
            # Third Layer: Specific attributes
            if src_bytes == 28:
                return ('teardrop', 'anomalous')
            elif src_bytes == 1:
                return ('saint or satan', 'anomalous')
            elif (src_bytes >= 40 and src_bytes <= 60) and dst_bytes == 0:
                return ('snmpguess', 'anomalous')
            elif dst_bytes == 0:
                return ('normal', 'normal')
            else:
                return ('snnmpgetattack', 'anomalous')
        elif service == 15 or service == 24: # ntp_u and domain_u
            return ('normal', 'normal')
        elif service == 18: # other

            # Third Layer: Specific attributes
            if src_bytes == 1 and dst_bytes == 1:
                return ('satan', 'anomalous')
            elif src_bytes == 40 and dst_bytes == 40:
                return ('saint', 'anomalous')
            else:
                return ('normal', 'normal')
        else:
            return ('N/A', 'anomalous')

    # ICMP
    elif protocol == 1:

        # Second layer: Service
        if service == 13: # eco_i
            # Third Layer: Specific attributes
            if src_bytes == 8:
                return ('ipsweep', 'anomalous')
            elif src_bytes == 20:
                return ('saint', 'anomalous')
            else:
                return ('normal', 'normal')
        elif service == 32: # tim_i
            # Third Layer: Specific attributes
            if src_bytes == 564 and dst_home_same_src_port_rate >= 127:
                return ('normal', 'normal')
            else:
                return ('pod', 'anomalous')
        elif service == 1: # urp_i
            # Third Layer: Specific attributes
            if src_bytes == 36:
                return ('smnpguess', 'anomalous')
            elif src_bytes == 37:
                return ('satan', 'anomalous')
            else:
                return ('normal', 'normal')
        elif service == 5: # ecr_i
            # Third Layer: Specific attributes
            if src_bytes == 18:
                return ('ipsweep', 'anomalous')
            elif src_bytes == 30 or src_bytes == 1480 or src_bytes == 64 or src_bytes == 72 or src_bytes > 6000:
                return ('normal', 'normal')
            else:
                return ('smurf', 'anomalous')
        else:
            return ('N/A', 'anomalous')

    # TCP
    else:
        # Second layer: Service
        if service == 16: # finger
            # Third Layer: Flag
            if flag == 7 or flag == 9: # S0 or RSTO
                return ('neptune', 'anomalous')
            elif flag == 5: # SF
                if duration == 1:
                    return ('saint', 'anomalous')
                else:
                    return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')

        elif service == 12: # ftp_data
            # Third Layer: Flag
            if flag == 7 or flag == 8: # S0 or REJ
                return ('neptune', 'anomalous')
            elif flag == 3: # S1
                return ('warezmaster', 'anomalous')
            elif flag == 5: # SF
                # Fourth Layer: attributes
                if src_bytes == 2209:
                    return ('xterm', 'anomalous')
                elif dst_bytes == 7:
                    return ('ftp_write', 'anomalous')
                elif dst_host_srv_err_rate == 5:
                    return ('rootkit', 'anomalous')
                elif dst_bytes == 8035:
                    return ('multihop', 'anomalous')
                elif src_bytes == 2848:
                    return ('httptunnel', 'anomalous')
                else:
                    return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')

        elif service == 38: # http
            # Third Layer: Flag
            if flag == 3: # S1
                return ('normal', 'normal')
            elif flag == 5: # SF
                # Fourth Layer: attributes
                if src_bytes == 54540 and dst_bytes == 8314:
                    return ('back', 'anomalous')
                elif dst_bytes == 8371:
                    return ('phf', 'anomalous')
                elif dst_bytes == 255:
                    return ('apache2', 'anomalous')
                else:
                    return ('normal', 'normal')
            elif flag == 9: # RSTO
                return ('portsweep', 'anomalous')
            elif flag == 0: # S2
                return ('back', 'anomalous')
            elif flag == 6: # S3
                # Fourth Layer: attributes
                if duration > 0:
                    return ('apache2', 'anomalous')
                else:
                    return ('normal', 'normal')
            elif flag == 8: # REJ
                return ('neptune', 'anomalous')
            elif flag == 1: # RSTR
                # Fourth Layer: attributes
                if src_bytes == 0:
                    return ('portsweep', 'anomalous')
                elif dst_bytes < 2:
                    return ('apache2', 'anomalous')
                elif dst_bytes > 1000 and src_bytes > 1000:
                    return ('back', 'anomalous')
                else:
                    return ('N/A', 'anomalous')
            elif flag == 7: # S0
                if dst_host_srv_err_rate < 100:
                    return ('neptune', 'anomalous')
                else:
                    return ('apache2', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 11: # ftp
            # Third Layer: Flag
            if flag == 9 or flag == 7: # RSTO
                return ('neptune', 'anomalous')
            elif flag == 5: # SF
                if duration >= 280 and duration <= 290:
                    return ('warezmaster', 'anomalous')
                else:
                    return ('normal', 'normal')
            elif flag == 3: # S1
                return ('warezmaster', 'anomalous')
            elif flag == 8: # REJ
                return ('saint', 'anomalous')
            elif flag == 1: # RSTR
                return ('portsweep', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 25: # private
            # Third Layer: Flag
            if flag == 7 or flag == 8: # S0 or REJ
                return ('neptune', 'anomalous')
            elif flag == 1 or flag == 10: # RSTR or OTH
                return ('portsweep', 'anomalous')
            elif flag == 2: # SH
                return ('nmap', 'anomalous')
            elif flag == 3 or flag == 9: # S1 or RSTO
                return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')

        elif service == 30: # pop_3
            # Third Layer: Flag
            if flag == 7 or flag == 9: # S0 or RSTO
                return ('neptune', 'anomalous')
            elif flag == 1: #  RSTR
                return ('guess_passwd', 'anomalous')
            elif flag == 5: # SF
                # Fourth Layer: attributes
                if src_bytes == 66:
                    return ('normal', 'normal')
                else:
                    return ('guess_passwd', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 21: # smtp
            # Third Layer: Flag
            if flag == 7 or flag == 9: # S0 or RSTO
                return ('neptune', 'anomalous')
            elif flag == 0 or flag == 8: # S2 or REJ
                return ('normal', 'normal')
            elif flag == 5: # SF
                # Fourth Layer: attributes
                if src_bytes == 2599 and dst_bytes == 293:
                    return ('mailbomb', 'anomalous')
                else:
                    return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')

        elif service == 22: # time
            # Third Layer: Flag
            if flag == 7 or flag == 9:  # S0 or RSTO
                return ('neptune', 'anomalous')
            elif flag == 5: # SF
                return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')


        elif service == 36: # auth
            # Third Layer: Flag
            if flag == 7 or flag == 8:  # S0 or REJ
                return ('neptune', 'anomalous')
            elif flag == 5: # SF
                return ('normal', 'normal')
            else:
                return ('N/A', 'anomalous')

        elif service == 6: # link
            # Third Layer: Flag
            if flag == 7 or flag == 8:  # S0 or REJ
                return ('neptune', 'anomalous')
            elif flag == 5:  # SF
                return ('portsweep', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 40: # iso_tsap
            # Third Layer: Flag
            if flag == 7 or flag == 8:  # S0 or REJ
                return ('neptune', 'anomalous')
            elif flag == 1: # RSTR
                return ('portsweep', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 23: # telnet
            # Third Layer: Flag
            if flag == 7: # S0
                return ('neptune', 'anomalous')
            elif flag == 0 or flag == 1 or flag == 6: # S3 or RSTR or S2
                return ('processtable', 'anomalous')
            elif flag == 5: # SF
                # Fourth Layer: attributes
                if dst_bytes == 44:
                    return ('processtable', 'anomalous')
                elif dst_bytes == 174:
                    return ('guess_passwd', 'anomalous')
                else:
                    return ('N/A', 'anomalous')
            elif flag == 9: # RSTO
                # Fourth Layer: attributes
                if src_bytes != 0:
                    return ('guess_passwd', 'anomalous')
                else:
                    return ('neptune', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 18: # other
            # Third Layer: Flag
            if flag == 7 or flag == 9:  # S0 or RSTO
                return ('neptune', 'anomalous')
            elif flag == 8: # REJ
                return ('satan', 'anomalous')
            elif flag == 5: # SF
                return ('httptunnel', 'anomalous')
            else:
                return ('N/A', 'anomalous')

        elif service == 64 or service == 0 or service == 35: # ICMP or ICR or XII
            return ('normal', 'normal')

        else:
            return ('neptune', 'anomalous')

if __name__ == '__main__':
    main()