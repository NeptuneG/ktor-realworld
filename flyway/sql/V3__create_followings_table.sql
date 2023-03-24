CREATE TABLE "followings" (
  "id" SERIAL PRIMARY KEY,
  "follower_id" uuid NOT NULL,
  "followee_id" uuid NOT NULL
);

CREATE UNIQUE INDEX "followings_unique_follower_id_followee_id_index" ON "followings" ("follower_id", "followee_id");
