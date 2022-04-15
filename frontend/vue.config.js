module.exports = {
    devServer: {
        proxy: {
            '^/': {
                target: 'http://localhost:3000',
                ws: true,
                changeOrigin: true,
                secure: false
            }
        }
    },
    assetsDir: 'static'
};
