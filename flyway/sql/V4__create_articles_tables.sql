CREATE TABLE "articles" (
  "id" SERIAL PRIMARY KEY,
  "author_id" uuid NOT NULL,
  "slug" varchar(64) NOT NULL,
  "title" varchar(64) NOT NULL,
  "description" varchar(255) NOT NULL,
  "body" text NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX "articles_unique_slug_index" ON "articles" ("slug");
CREATE UNIQUE INDEX "articles_unique_title_index" ON "articles" ("title");

CREATE TABLE "tags" (
  "id" SERIAL PRIMARY KEY,
  "value" varchar(16) NOT NULL
);

CREATE UNIQUE INDEX "tags_unique_value_index" ON "tags" ("value");

CREATE TABLE "article_tags" (
  "id" SERIAL PRIMARY KEY,
  "article_id" integer NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE,
  "tag_id" integer NOT NULL REFERENCES "tags" ("id") ON DELETE CASCADE,
  CONSTRAINT "article_tags_unique_constraint" UNIQUE ("article_id", "tag_id")
);

CREATE TABLE "article_favorites" (
  "id" SERIAL PRIMARY KEY,
  "article_id" integer NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE,
  "favoritee_id" uuid NOT NULL,
  CONSTRAINT "article_favorites_unique_constraint" UNIQUE ("article_id", "favoritee_id")
);
