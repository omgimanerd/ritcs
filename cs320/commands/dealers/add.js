/**
 * @fileoverview Add a dealer to the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'add <name> <phone>'

exports.description = 'Add a dealer'

exports.handler = argv => {
  const client = util.getClient()
  client.query(
    'insert into dealers(name,phone) values($1, $2)',
    [argv.name, argv.phone]
  ).then(() => {
    console.log('Dealer inserted')
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
