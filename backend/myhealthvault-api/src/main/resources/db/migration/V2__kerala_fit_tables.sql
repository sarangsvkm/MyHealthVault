-- Migration V2: Kerala Fit Nutrition, Diet & Workout Tables
CREATE TABLE fitness_food_log (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(50) NOT NULL,
  calories INTEGER NOT NULL,
  meal_type VARCHAR(50) NOT NULL,
  logged_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX ix_fitness_food_user ON fitness_food_log(user_id, logged_at DESC);

CREATE TABLE fitness_workout_log (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  plan_name VARCHAR(255) NOT NULL,
  duration_minutes INTEGER NOT NULL,
  completed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX ix_fitness_workout_user ON fitness_workout_log(user_id, completed_at DESC);
