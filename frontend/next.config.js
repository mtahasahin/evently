module.exports = {
  images: {
    domains: [
      'eventlydemo.xyz',
      'localhost',
      'i.pravatar.cc',
      'evently-amazon-storage.s3.eu-central-1.amazonaws.com',
      'picsum.photos',
    ],
  },
  eslint: {
    dirs: ['pages', 'app'], // Only run ESLint on the 'pages' and 'utils' directories during production builds (next build)
  },
};
