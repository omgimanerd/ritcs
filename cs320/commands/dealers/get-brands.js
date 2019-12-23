/**
 * @fileoverview Get the brands sold by a dealer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get-brands <dealer id>'

exports.description = 'Get the brands sold by a dealer'

exports.handler = argv => {
  const client = util.getClient()
  client.query(`
    select name, country, reliability from
      brands b inner join brand_dealer d on b.name = d.brand
      where dealer = $1`, [argv.dealerid]
  ).then(result => {
    display.displayBrands(result.rows)
    client.end()
  }).catch(error => {
    console.error(error.message)
    client.end()
  })
}
