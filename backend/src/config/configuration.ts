export default () => ({
  port: parseInt(process.env.PORT, 10) || 8000,
  databaseUrl: process.env.DATABASE_URL,
});
