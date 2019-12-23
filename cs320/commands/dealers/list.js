/**
 * @fileoverview List dealers in the database.
 * @author Alvin Lin (axl1439)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'list'

exports.description = 'List all dealers'

exports.handler = () => {
  const client = util.getClient()
  const subquery = `
    select sale, d.id, d.name, d.phone, sum(price) as price from
      vehicles v inner join dealers d on v.dealer = d.id
      where sale is not null group by d.id,sale`
  client.query(`
    select id, name, phone, avg(price) as price from
      (${subquery}) as sub
      group by id, name, phone order by id`
  ).then(result => {
    display.displayDealers(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
