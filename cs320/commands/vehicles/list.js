/**
 * @fileoverview List and search for vehicles in the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'list'

exports.description = 'List all vehicles'

exports.builder = yargs => {
  yargs.option('color', {
    description: 'Limit results by color',
    requiresArg: true
  }).option('brand', {
    description: 'Limit results by brand',
    requiresArg: true
  }).option('model', {
    description: 'Limit results by model',
    requiresArg: true
  }).option('engine', {
    description: 'Limit results by engine type',
    requiresArg: true
  }).option('transmission', {
    description: 'Limit results by transmission',
    requiresArg: true
  }).option('mileage', {
    description: 'Limit to results below a certain mileage',
    requiresArg: true,
    number: true
  }).option('price', {
    description: 'Limit to results below a certain price',
    requiresArg: true,
    number: true
  }).option('dealer_id', {
    description: 'Limit results by dealer id',
    requiresArg: true,
    number: true
  }).option('dealer_name', {
    description: 'Limit results by dealer name',
    requiresArg: true,
    string: true
  }).option('owner_id', {
    description: 'Limit results by owner id',
    requiresArg: true,
    number: true
  }).option('owner_name', {
    description: 'Limit results by owner name',
    requiresArg: true,
    string: true
  }).option('unowned', {
    description: 'Limit results to unowned vehicles',
    'boolean': true
  }).option('reliability', {
    description: 'Limit results by brand reliability',
    requiresArg: true,
    choices: ['Great', 'Fair', 'Poor']
  })
}

exports.handler = argv => {
  const client = util.getClient()
  let argCounter = 1
  const conditions = []
  const substitutions = []
  _.keys(_.omit(argv, ['_', '$0'])).forEach(field => {
    switch (field) {
    case 'mileage':
    case 'price':
      conditions.push(`${field} < $${argCounter++}`)
      break
    case 'dealer_id':
      conditions.push(`d.id = $${argCounter++}`)
      break
    case 'dealer_name':
      conditions.push(`d.name like $${argCounter++}`)
      substitutions.push(`${argv[field]}%`)
      return
    case 'owner_id':
      conditions.push(`c.id = $${argCounter++}`)
      break
    case 'owner_name':
      conditions.push(`c.name like $${argCounter++}`)
      substitutions.push(`${argv[field]}%`)
      return
    case 'unowned':
      conditions.push('c.name is null')
      return
    case 'reliability':
      conditions.push(`b.reliability = $${argCounter++}`)
      break
    default:
      conditions.push(`${field} = $${argCounter++}`)
    }
    substitutions.push(argv[field])
  })
  const whereClause = conditions.length === 0 ?
    '' : `where ${conditions.join(' and ')}`
  const query = `
    select vin, color, brand, model, price, d.name as dealer,
      c.name as owner from
      vehicles v inner join dealers d on v.dealer = d.id
        inner join brands b on v.brand = b.name
        left outer join customers c on v.owner = c.id
      ${whereClause}
      order by dealer`
  client.query(query, substitutions).then(result => {
    display.displayVehicles(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
