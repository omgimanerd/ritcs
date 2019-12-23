/**
 * @fileoverview List all brands in the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'list'

exports.description = 'Lists all brands'

exports.builder = yargs => {
  yargs.option('country', {
    description: 'Limit results by country',
    requiresArg: true
  }).option('reliability', {
    description: 'Limit results by reliability',
    requiresArg: true,
    choices: ['Great', 'Fair', 'Poor']
  })
}

exports.handler = argv => {
  const client = util.getClient()
  const conditions = []
  const substitutions = []
  _.keys(_.omit(argv, ['_', '$0'])).forEach((field, i) => {
    conditions.push(`${field} = $${i + 1}`)
    substitutions.push(argv[field])
  })
  const whereClause = conditions.length === 0 ?
    '' : `where ${conditions.join(' and ')}`
  const query = `select * from brands ${whereClause}`
  client.query(query, substitutions).then(result => {
    display.displayBrands(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
