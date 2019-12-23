/**
 * @fileoverview Update a brand's reliability.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'update <name> <reliability>'

exports.description = 'Update a brand\'s reliability'

exports.builder = yargs => {
  yargs.choices('reliability', ['Great', 'Fair', 'Poor'])
}

exports.handler = argv => {
  const client = util.getClient()
  const query = 'update brands set reliability = $1 where name = $2'
  client.query(query, [argv.reliability, argv.name]).then(result => {
    if (result.rowCount === 1) {
      console.log('Brand updated.')
    } else {
      console.log('No such brand.')
    }
    client.end()
  })
}
