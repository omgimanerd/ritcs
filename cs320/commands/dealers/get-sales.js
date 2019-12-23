/**
 * @fileoverview Get sales made by a dealer.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get-sales <dealer id>'

exports.description = 'Get sales made by a dealer'

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    select v.sale as id, d.name as dealer, c.name as customer,
      s.close_date, sum(v.price) as price from
      vehicles v inner join dealers d on v.dealer = d.id inner join
        customers c on v.owner = c.id inner join
        sales s on s.id = v.sale
      where d.id = $1
      group by v.sale, d.name, c.name, s.close_date
      order by s.close_date`
  client.query(query, [argv.dealerid]
  ).then(result => {
    display.displaySales(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
