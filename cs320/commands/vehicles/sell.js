/**
 * @fileoverview Sell a vehicle(s) to a customer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'sell <customer id>'

exports.aliases = ['buy']

exports.description = 'Register the sale of one or more vehicles'

exports.builder = yargs => {
  yargs.option('vin', {
    description: 'The vehicle vin numbers being sold',
    required: true,
    array: true
  })
}

exports.handler = argv => {
  util.sellVehicle(argv.customerid, argv.vin)
}
