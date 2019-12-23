/**
 * @fileoverview Buy a vehicle for a customer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'buy-vehicle <customer id>'

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
