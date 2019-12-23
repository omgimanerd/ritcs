/**
 * @fileoverview Add a brand to a dealer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'add-brands <dealer id> <brand>'

exports.description = 'Add a brand to the list sold by a dealer'

exports.handler = argv => {
  const client = util.getClient()
  client.query(
    'insert into brand_dealer values($1, $2)', [argv.dealerid, argv.brand]
  ).then(() => {
    console.log(`Dealer ID ${argv.dealerid} now sells ${argv.brand}`)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
