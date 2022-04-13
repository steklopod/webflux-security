module.exports = {
    devServer: {
        proxy: {
            '^/': {
                target: 'http://localhost:8087',
                ws: true,
                changeOrigin: true,
                secure: false,
                logLevel: 'debug'
            }
        }
    },
    assetsDir: 'static'
};
