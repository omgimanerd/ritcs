/**
 * @fileoverview Add a brand to the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'add <name> <country> <reliability>'

exports.description = 'Adds a brand'

exports.builder = yargs => {
  yargs.choices('reliability', ['Great', 'Fair', 'Poor'])
}

exports.handler = argv => {
  const client = util.getClient()
  client.query('insert into brands values($1, $2, $3)', [
    argv.name, argv.country, argv.reliability
  ]).then(() => {
    console.log('Brand added.')
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
