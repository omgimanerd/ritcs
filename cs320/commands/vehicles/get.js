/**
 * @fileoverview Get a vehicle by its VIN
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get <vin>'

exports.description = 'Get a vehicle by its VIN number'

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    select vin, color, brand, model, engine, transmission, mileage, price,
      sale, d.name as dealer, c.name as owner from
      vehicles v inner join dealers d on v.dealer = d.id
        left outer join customers c on v.owner = c.id
      where vin = $1`
  client.query(query, [argv.vin]).then(result => {
    if (result.rowCount === 1) {
      display.displayVehicle(result.rows[0])
    } else {
      console.error('No such matching vehicle.')
    }
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
