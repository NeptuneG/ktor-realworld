CREATE TABLE "comments" (
  "id" SERIAL PRIMARY KEY,
  "article_id" integer NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE,
  "author_id" uuid NOT NULL,
  "body" text NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
