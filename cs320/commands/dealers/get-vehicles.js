/**
 * @fileoverview Get unsold vehicles for a dealer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get-vehicles <dealer id>'

exports.description = 'Get a list of a dealer\'s unsold vehicles'

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    select vin, color, brand, model, price, d.name as dealer,
      c.name as owner from
      vehicles v inner join dealers d on v.dealer = d.id
        left outer join customers c on v.owner = c.id
      where d.id = $1 and c.id is null`
  client.query(query, [argv.dealerid]).then(result => {
    display.displayVehicles(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
