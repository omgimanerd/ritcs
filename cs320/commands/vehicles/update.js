/**
 * @fileoverview Update a vehicle's price.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'update <vin> <price>'

exports.description = 'Update the price of an unsold vehicle'

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    update vehicles set price = $2 where vin = $1 and owner is null`
  client.query(query, [argv.vin, argv.price]).then(result => {
    if (result.rowCount === 1) {
      console.log('Vehicle price updated.')
    } else {
      console.log('You cannot update the price of a sold vehicle.')
    }
    client.end()
  })
}
