/**
 * @fileoverview Lists sales in the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'list'

exports.description = 'List sales with an optional filter'

exports.builder = yargs => {
  yargs.option('customer_id', {
    description: 'Find sales made to a customer ID',
    requiresArg: true,
    number: true
  }).option('customer_name', {
    description: 'Find sales made to a customer name',
    requiresArg: true
  }).option('dealer_id', {
    description: 'Find sales made by a dealer ID',
    requiresArg: true,
    number: true
  }).option('dealer_name', {
    description: 'Find sales made by a dealer name',
    requiresArg: true
  }).option('above_price', {
    description: 'Find sales above a certain price',
    requiresArg: true,
    number: true,
    conflicts: ['below_price']
  }).option('below_price', {
    description: 'Find sales below a certain price',
    requiresArg: true,
    number: true,
    conflicts: ['above_price']
  }).option('before', {
    description: 'Find sales made before a date',
    requiresArg: true
  }).option('after', {
    description: 'Find sales made after a date',
    requiresArg: true
  })
}

exports.handler = argv => {
  const client = util.getClient()
  let argCounter = 1
  const substitutions = []
  const whereConditions = []
  const havingConditions = []
  _.keys(argv).forEach(field => {
    switch (field) {
    case 'customer_id':
      whereConditions.push(`c.id = $${argCounter++}`)
      break
    case 'customer_name':
      whereConditions.push(`c.name like $${argCounter++}`)
      substitutions.push(`${argv[field]}%`)
      return
    case 'dealer_id':
      whereConditions.push(`d.id = $${argCounter++}`)
      break
    case 'dealer_name':
      whereConditions.push(`d.name like $${argCounter++}`)
      substitutions.push(`${argv[field]}%`)
      return
    case 'above_price':
      havingConditions.push(`sum(price) > $${argCounter++}`)
      break
    case 'below_price':
      havingConditions.push(`sum(price) < $${argCounter++}`)
      break
    case 'before':
      whereConditions.push(`close_date < $${argCounter++}`)
      break
    case 'after':
      whereConditions.push(`close_date > $${argCounter++}`)
      break
    default:
      return
    }
    substitutions.push(`${argv[field]}`)
  })
  const whereClause = whereConditions.length === 0 ?
    '' : `where ${whereConditions.join(' and ')}`
  const havingClause = havingConditions.length === 0 ?
    '' : `having ${havingConditions.join(' and ')}`
  const query = `
    select v.sale as id, d.name as dealer, c.name as customer, s.close_date,
      sum(v.price) as price from
      vehicles v inner join dealers d on v.dealer = d.id inner join
        customers c on v.owner = c.id inner join
        sales s on s.id = v.sale
      ${whereClause}
      group by v.sale, d.name, c.name, s.close_date
      ${havingClause}
      order by close_date`
  client.query(query, substitutions).then(result => {
    display.displaySales(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
