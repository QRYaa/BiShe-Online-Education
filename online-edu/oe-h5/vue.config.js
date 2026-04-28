let configUrl = require('./common/config.js');
module.exports = {
    chainWebpack: config => {
      config
        .plugin('define')
        .tap(args => {
            args[0]['process.env.config'] = JSON.stringify(configUrl)
            return args
        })
    }
}
