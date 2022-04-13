module.exports = {
    devServer: {
        proxy: {
            '^/': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true,
                secure: false,
                logLevel: 'debug'
            }
        }
    },
    assetsDir: 'static'
};
