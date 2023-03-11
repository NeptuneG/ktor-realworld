CREATE TABLE "users" (
  "id" SERIAL PRIMARY KEY,
  "username" varchar(255) NOT NULL,
  "email" varchar(255) NOT NULL,
  "password" varchar(255) NOT NULL,
  "bio" varchar(255),
  "image" varchar(255),
  "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX "users_unique_username_index" ON "users" ("username");
CREATE UNIQUE INDEX "users_unique_email_index" ON "users" ("email");
