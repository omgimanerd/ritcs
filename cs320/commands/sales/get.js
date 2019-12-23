/**
 * @fileoverview Get a sale by its ID.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get <id>'

exports.description = 'Get a sale by its ID'

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    select vin, model, transmission, engine, color, price, brand,
      d.name as dealer, c.name as owner from
      vehicles v inner join dealers d on v.dealer = d.id
        left outer join customers c on v.owner = c.id
      where v.sale = $1`
  client.query(query, [argv.id]).then(result => {
    if (result.rowCount === 0) {
      console.error('No such matching sale.')
    } else {
      display.displayVehicles(result.rows)
    }
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
