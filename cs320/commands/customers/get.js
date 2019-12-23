/**
 * @fileoverview Get a customer by their ID.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get <id>'

exports.description = 'Get a customer by their ID'

exports.handler = argv => {
  const client = util.getClient()
  client.query('select * from customers where id = $1', [
    argv.id
  ]).then(result => {
    if (result.rowCount === 1) {
      display.displayCustomer(result.rows[0])
    } else {
      console.error('No such matching customer.')
    }
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
