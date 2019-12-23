/**
 * @fileoverview Get the vehicles owned by a customer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get-vehicles'

exports.description = 'List vehicles owned by a customer'

exports.builder = yargs => {
  yargs.option('id', {
    description: 'Select customer by ID',
    requiresArg: true,
    number: true,
    conflicts: ['name']
  }).option('name', {
    description: 'Select customer by name match',
    requiresArg: true,
    string: true,
    conflicts: ['id']
  })
}

exports.handler = argv => {
  if (!argv.id && !argv.name) {
    console.log('A customer name or ID must be provided.')
    console.log('Run: node cli.js customers get-vehicles --help')
    return
  }
  let conditionalQuery = ''
  const substitution = []
  const query = `
    select vin, color, brand, model, price, d.name as dealer,
      c.name as owner from
      vehicles v inner join dealers d on v.dealer = d.id
        left outer join customers c on v.owner = c.id`
  if (argv.id) {
    conditionalQuery = `${query} where v.owner = $1 order by c.name`
    substitution.push(argv.id)
  } else {
    conditionalQuery = `${query} where c.name like $1 order by c.name`
    substitution.push(`${argv.name}%`)
  }
  const client = util.getClient()
  client.query(conditionalQuery, substitution).then(result => {
    display.displayVehicles(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
